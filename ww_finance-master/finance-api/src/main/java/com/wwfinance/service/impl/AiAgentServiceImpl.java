package com.wwfinance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import com.wwfinance.entity.User;
import com.wwfinance.entity.UserAccount;
import com.wwfinance.entity.dto.AiAgentRequest;
import com.wwfinance.entity.dto.AiAgentResponse;
import com.wwfinance.mapper.UserAccountMapper;
import com.wwfinance.mapper.UserMapper;
import com.wwfinance.service.AiAgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * AI Agent服务实现类
 */
@Service
@Slf4j
public class AiAgentServiceImpl implements AiAgentService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserAccountMapper userAccountMapper;
    
    // 大模型配置
    @Value("${ai.model.api-key}")
    private String apiKey;
    
    @Value("${ai.model.base-url}")
    private String baseUrl;
    
    @Value("${ai.model.model-name}")
    private String modelName;
    
    @Value("${ai.model.temperature}")
    private double temperature;
    
    @Value("${ai.model.max-tokens}")
    private int maxTokens;
    
    // Python微调服务配置
    @Value("${ai.fine-tune.service-url}")
    private String fineTuneServiceUrl;
    
    @Value("${ai.fine-tune.enabled}")
    private boolean fineTuneEnabled;
    
    // OpenAI服务实例
    private OpenAiService openAiService;
    
    // WebClient实例，用于调用Python微调服务
    private WebClient webClient;
    
    // ObjectMapper实例，用于JSON序列化和反序列化
    private ObjectMapper objectMapper;
    
    // 初始化OpenAI服务
    public void initOpenAiService() {
        if (openAiService == null) {
            try {
                // 设置API密钥和超时时间
                openAiService = new OpenAiService(apiKey, Duration.ofSeconds(30));
                log.info("OpenAI服务初始化成功");
            } catch (Exception e) {
                log.error("OpenAI服务初始化失败: {}", e.getMessage(), e);
                throw new RuntimeException("OpenAI服务初始化失败", e);
            }
        }
    }
    
    // 初始化WebClient
    public void initWebClient() {
        if (webClient == null) {
            webClient = WebClient.builder()
                .baseUrl(fineTuneServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
            log.info("WebClient初始化成功，基础URL: {}", fineTuneServiceUrl);
        }
        
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
    }
    
    /**
     * 调用Python微调服务进行推理
     * @param userQuestion 用户问题
     * @param accountInfo 账户信息
     * @return 微调模型响应
     */
    private String callFineTunedModel(String userQuestion, String accountInfo) {
        initWebClient();
        
        try {
            // 构建请求参数
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("user_question", userQuestion);
            requestBody.put("account_info", accountInfo);
            
            log.info("调用Python微调服务，用户问题长度: {}, 账户信息长度: {}", 
                    userQuestion.length(), accountInfo.length());
            
            // 发送POST请求
            String responseJson = webClient.post()
                .uri("/api/inference")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(String.class)
                .block();
            
            log.info("Python微调服务响应成功，响应长度: {}", responseJson.length());
            
            // 解析响应
            JsonNode responseNode = objectMapper.readTree(responseJson);
            String status = responseNode.get("status").asText();
            String content = responseNode.get("response").asText();
            
            if ("success".equals(status)) {
                return content;
            } else {
                log.error("Python微调服务返回错误状态: {}", status);
                return "调用微调模型失败，请稍后重试。";
            }
        } catch (Exception e) {
            log.warn("Python微调服务不可用，将回退到原始大模型: {}", e.getMessage());
            // 返回特殊标记，指示回退到原始大模型
            return "__FALLBACK_TO_ORIGINAL_MODEL__";
        }
    }
    
    /**
     * 调用大模型API
     * @param prompt 提示词
     * @return 大模型响应
     */
    private String callLargeModel(String prompt) {
        // 如果启用了微调服务，尝试使用微调模型
        if (fineTuneEnabled) {
            // 从提示词中提取用户问题和账户信息
            String userQuestion = prompt;
            String accountInfo = "";
            
            if (prompt.contains("用户账户情况：")) {
                int start = prompt.indexOf("用户账户情况：");
                int end = prompt.indexOf("\n\n用户的查询问题：");
                if (start != -1 && end != -1) {
                    accountInfo = prompt.substring(start, end);
                    userQuestion = prompt.substring(end + 6);
                }
            }
            
            String fineTunedResponse = callFineTunedModel(userQuestion, accountInfo);
            if (!"__FALLBACK_TO_ORIGINAL_MODEL__".equals(fineTunedResponse) && !fineTunedResponse.startsWith("调用微调模型失败")) {
                return fineTunedResponse;
            }
            log.warn("微调模型调用失败，回退到原始大模型");
        }
        
        // 回退到原始大模型
        initOpenAiService();
        
        // 最大重试次数
        int maxRetries = 3;
        int retryCount = 0;
        
        while (retryCount < maxRetries) {
            try {
                // 创建系统消息和用户消息
                List<ChatMessage> messages = new ArrayList<>();
                
                // 系统消息，定义助手角色
                ChatMessage systemMessage = new ChatMessage(
                    ChatMessageRole.SYSTEM.value(),
                    "你是一个专业的金融助手，负责为用户提供金融建议、风险评估和投资建议。请根据用户的具体情况，提供专业、准确、有针对性的回答。"
                );
                messages.add(systemMessage);
                
                // 用户消息
                ChatMessage userMessage = new ChatMessage(
                    ChatMessageRole.USER.value(),
                    prompt
                );
                messages.add(userMessage);
                
                // 创建聊天完成请求
                ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model(modelName)
                    .messages(messages)
                    .temperature(temperature)
                    .maxTokens(maxTokens)
                    .build();
                
                // 调用大模型API
                log.info("调用大模型API (尝试 {} / {})，提示词长度: {}", 
                        retryCount + 1, maxRetries, prompt.length());
                String response = openAiService.createChatCompletion(request)
                    .getChoices().get(0).getMessage().getContent();
                log.info("大模型API响应成功，响应长度: {}", response.length());
                
                return response;
            } catch (Exception e) {
                retryCount++;
                log.error("调用大模型API失败 (尝试 {} / {}): {}", 
                        retryCount, maxRetries, e.getMessage(), e);
                
                // 如果达到最大重试次数，返回错误信息
                if (retryCount >= maxRetries) {
                    log.error("达到最大重试次数，调用大模型API失败");
                    return "调用大模型失败，请稍后重试。系统正在努力恢复服务。";
                }
                
                // 重试前等待一段时间
                try {
                    Thread.sleep(1000 * retryCount); // 指数退避
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return "调用大模型失败，请稍后重试。";
                }
            }
        }
        
        return "调用大模型失败，请稍后重试。";
    }
    
    @Override
    public AiAgentResponse processRequest(AiAgentRequest request) {
        long startTime = System.currentTimeMillis();
        AiAgentResponse response = new AiAgentResponse();
        
        try {
            String result = "";
            
            switch (request.getType()) {
                case "financialAdvice":
                    result = getFinancialAdvice(request.getUserId(), request.getQuery());
                    break;
                case "borrowRisk":
                    // 解析参数
                    String[] borrowParams = request.getParams().split(",");
                    double borrowAmount = Double.parseDouble(borrowParams[0]);
                    int borrowTerm = Integer.parseInt(borrowParams[1]);
                    result = evaluateBorrowRisk(request.getUserId(), borrowAmount, borrowTerm);
                    break;
                case "investmentAdvice":
                    // 解析参数
                    String[] investParams = request.getParams().split(",");
                    String riskLevel = investParams[0];
                    double investmentAmount = Double.parseDouble(investParams[1]);
                    result = getInvestmentAdvice(request.getUserId(), riskLevel, investmentAmount);
                    break;
                case "question":
                    result = answerUserQuestion(request.getUserId(), request.getQuery());
                    break;
                default:
                    result = "未知请求类型，请检查参数。";
            }
            
            response.setStatus("success");
            response.setContent(result);
            response.setType(request.getType());
        } catch (Exception e) {
            log.error("AI Agent处理请求失败: {}", e.getMessage(), e);
            response.setStatus("error");
            response.setContent("处理请求失败，请稍后重试。");
            response.setType(request.getType());
        }
        
        response.setProcessingTime(System.currentTimeMillis() - startTime);
        return response;
    }
    
    @Override
    public String getFinancialAdvice(Long userId, String query) {
        // 获取用户信息
        User user = userMapper.selectById(userId);
        UserAccount account = userAccountMapper.selectOne(
            new LambdaQueryWrapper<UserAccount>()
                .eq(UserAccount::getUserId, userId)
        );
        
        // 构建大模型提示词
        StringBuilder prompt = new StringBuilder();
        prompt.append("尊敬的金融专家，我需要您根据以下用户情况提供专业的金融建议：\n\n");
        
        if (account != null) {
            prompt.append("用户账户情况：\n");
            prompt.append("- 账户余额：").append(account.getAccountBalance()).append("元\n");
            prompt.append("- 总收入：").append(account.getTotalIncome()).append("元\n");
            prompt.append("- 冻结金额：").append(account.getFrozenAmount()).append("元\n\n");
        }
        
        prompt.append("用户的查询问题：").append(query).append("\n\n");
        prompt.append("请为用户提供详细、专业、有针对性的金融建议，包括但不限于：\n");
        prompt.append("1. 基于用户当前财务状况的具体建议\n");
        prompt.append("2. 针对用户查询问题的专业回答\n");
        prompt.append("3. 实用的财务管理技巧\n");
        prompt.append("4. 可能的风险提示\n");
        prompt.append("\n请以自然、友好的语言回答，避免使用过于专业的术语，确保用户能够理解。");
        
        // 调用大模型API
        return callLargeModel(prompt.toString());
    }
    
    @Override
    public String evaluateBorrowRisk(Long userId, Double borrowAmount, Integer borrowTerm) {
        // 获取用户信息
        User user = userMapper.selectById(userId);
        UserAccount account = userAccountMapper.selectOne(
            new LambdaQueryWrapper<UserAccount>()
                .eq(UserAccount::getUserId, userId)
        );
        
        // 计算每月还款额
        double monthlyPayment = borrowAmount / borrowTerm;
        
        // 构建大模型提示词
        StringBuilder prompt = new StringBuilder();
        prompt.append("尊敬的金融风险评估专家，我需要您对用户的借款风险进行专业评估：\n\n");
        
        prompt.append("借款信息：\n");
        prompt.append("- 借款金额：").append(borrowAmount).append("元\n");
        prompt.append("- 借款期限：").append(borrowTerm).append("个月\n");
        prompt.append("- 每月还款：").append(String.format("%.2f", monthlyPayment)).append("元\n\n");
        
        if (account != null) {
            prompt.append("用户账户情况：\n");
            prompt.append("- 账户余额：").append(account.getAccountBalance()).append("元\n");
            prompt.append("- 总收入：").append(account.getTotalIncome()).append("元\n");
            prompt.append("- 冻结金额：").append(account.getFrozenAmount()).append("元\n\n");
        }
        
        prompt.append("请为用户提供详细的风险评估报告，包括：\n");
        prompt.append("1. 风险等级（高、中、低）\n");
        prompt.append("2. 详细的评估理由\n");
        prompt.append("3. 基于用户财务状况的具体建议\n");
        prompt.append("4. 可能的风险提示\n");
        prompt.append("5. 优化建议（如调整借款金额、期限等）\n");
        prompt.append("\n请以专业、客观的态度进行评估，确保评估结果准确、有参考价值。");
        
        // 调用大模型API
        return callLargeModel(prompt.toString());
    }
    
    @Override
    public String getInvestmentAdvice(Long userId, String riskLevel, Double investmentAmount) {
        // 获取用户信息
        UserAccount account = userAccountMapper.selectOne(
            new LambdaQueryWrapper<UserAccount>()
                .eq(UserAccount::getUserId, userId)
        );
        
        // 构建大模型提示词
        StringBuilder prompt = new StringBuilder();
        prompt.append("尊敬的投资顾问，我需要您根据以下用户情况提供专业的投资建议：\n\n");
        
        prompt.append("投资信息：\n");
        prompt.append("- 风险等级：").append(riskLevel).append("\n");
        prompt.append("- 投资金额：").append(investmentAmount).append("元\n\n");
        
        if (account != null) {
            prompt.append("用户账户情况：\n");
            prompt.append("- 账户余额：").append(account.getAccountBalance()).append("元\n");
            prompt.append("- 总收入：").append(account.getTotalIncome()).append("元\n\n");
        }
        
        prompt.append("请为用户提供详细的投资建议，包括：\n");
        prompt.append("1. 推荐的投资产品（至少3种）\n");
        prompt.append("2. 每种产品的特点和风险收益分析\n");
        prompt.append("3. 资金配置比例建议\n");
        prompt.append("4. 投资期限建议\n");
        prompt.append("5. 风险提示\n");
        prompt.append("\n请根据用户选择的风险等级，提供相应的投资建议。语言要专业、清晰、易懂，避免使用过于复杂的术语。");
        
        // 调用大模型API
        return callLargeModel(prompt.toString());
    }
    
    @Override
    public String answerUserQuestion(Long userId, String question) {
        // 获取用户信息
        User user = userMapper.selectById(userId);
        UserAccount account = userAccountMapper.selectOne(
            new LambdaQueryWrapper<UserAccount>()
                .eq(UserAccount::getUserId, userId)
        );
        
        // 构建大模型提示词
        StringBuilder prompt = new StringBuilder();
        prompt.append("尊敬的金融助手，我需要您回答用户的问题：\n\n");
        
        prompt.append("用户问题：").append(question).append("\n\n");
        
        if (account != null) {
            prompt.append("用户账户情况：\n");
            prompt.append("- 账户余额：").append(account.getAccountBalance()).append("元\n");
            prompt.append("- 总收入：").append(account.getTotalIncome()).append("元\n\n");
        }
        
        prompt.append("请根据用户的问题和账户情况，提供专业、准确、有针对性的回答。\n");
        prompt.append("如果用户的问题涉及账户余额等具体信息，请根据提供的账户情况进行回答。\n");
        prompt.append("回答要自然、友好、易懂，避免使用过于专业的术语。\n");
        prompt.append("如果您无法回答用户的问题，请礼貌地告知用户，并建议他们联系客服获取更多帮助。");
        
        // 调用大模型API
        return callLargeModel(prompt.toString());
    }
}
