
[目录]
- [角色Role](#角色Role)
- [结构化提示词Prompt](#结构化提示词Prompt)



LLM(Large Language Model)：指使用大量文本数据训练的深度学习模型，使得该模型可以生成自然语言文本或理解语言文本的含义。

# 角色Role

在大语言模型中，assistant、user和system三种角色的定位和功能如下‌：

1. System（系统角色）‌：
    - 定位‌：系统角色相当于对话过程中的“导演”，负责控制对话的全局方向和模型的行为模式。
    - 作用‌：
        - 设定上下文：定义对话的背景、目标或规则，例如指定模型的角色（如“你是一个专业翻译，只回答与翻译相关的问题”）。
        - 调整风格：指定回复的语气（严肃、幽默）、格式（分点、Markdown）、限制回答的内容（避免敏感话题）等。
        - 长期控制：系统角色的设定对整个对话过程都有效（即使后续对话中没有重复指令），但通常不会直接与用户互动‌。
    - 典型场景：
        - 初始化对话时设定角色（如：医生、律师、代码助手）。
        - 限制模型的回答范围（如：“仅用英文回答”）。
        - 设定复杂任务的流程（如：分步骤完成任务）。
    - 技术细节：
        - 在某些模型（如GPT-3.5/4）中，system 消息通常在对话开始时出现一次。
        - 并非所有平台都支持 system 角色（部分平台用 user 模拟其功能）。
2. User（用户角色）‌：
    - 定位‌：用户角色代表与大模型进行对话的真实用户，是驱动对话的核心。
    - 作用‌：用户通过输入问题或请求来引导对话的方向，提供补充信息或修正模型的行为。用户的发言构成了模型接收到的输入，模型根据用户的发言生成相应的回复‌
        - 提出问题或请求（如：“帮我写一首关于夏天的诗”）。
        - 提供补充信息（如：“上一句翻译成法语”）。
        - 修正模型行为（如：“用更简单的语言解释”）。
    - 典型场景：
        - 直接交互：用户提问、追问或反馈。
        - 间接控制：通过用户消息调整模型输出（例如在消息中附加指令）。
    - 技术细节：
        - 在API调用中，user 消息是必选的上下文组成部分。
        - 模型会优先关注最近的 user 消息内容。
3. Assistant（助手角色）‌：
    - 定位‌：助手角色是模型生成的回复内容，代表大模型本身。
    - ‌作用‌：基于上下文生成符合用户需求的回复，根据用户的反馈调整回答，遵循system或user指定的规则。助手角色的目的是理解用户的问题并生成合适的回复，同时考虑上下文和交互目标‌
        - 回答问题：基于上下文生成符合用户需求的回复。
        - 自我修正：在后续对话中根据用户反馈调整回答（如：“抱歉，之前的回答有误，正确的是…”）。
        - 遵循指令：执行 system 或 user 指定的规则（如：分点回答、使用特定格式）。
    - 典型场景：
        - 直接生成文本、代码、建议等。
        - 通过历史 assistant 消息实现多轮对话连贯性。
    - 技术细节：
        - 在训练和推理中，模型通过预测 assistant 消息的合理延续来生成回复。
        - 可通过预设 assistant 消息引导模型行为（例如提前写入部分回答）。
    
‌三者之间的关系和协作方式‌：
- System设定框架‌：系统角色设定对话的背景、目标和规则，为对话提供整体方向。
- User提供具体输入‌：用户角色通过提问、请求或提供补充信息来引导对话。
- Assistant生成回复‌：助手角色根据上下文生成回复，并在必要时根据用户反馈进行调整‌

优先级：
- 最近的 user 指令 > 初始 system 设定 > 历史 assistant 内容。
- 某些模型（如Claude）对 system 的权重更高，能更稳定地遵循长期指令。

使用建议：
1. 明确分工：
    - 复杂任务：用 system 设定角色和规则，user 提供具体输入。
    - 简单任务：直接在 user 消息中附加指令（如：“用列表总结”）。
2. 避免冲突：
    - 若 system 和 user 指令矛盾，模型可能优先响应 user。
    - 可通过 system 强调“始终遵循初始规则”来增强约束。
3. 平台差异：
    - OpenAI API 支持 system 角色，而ChatGPT网页版可能隐式处理 system 指令。
    - 部分开源模型（如Llama）需通过 user 消息模拟 system 功能。


# 结构化提示词Prompt

[https://github.com/langgptai/LangGPT/blob/main/README_zh.md](https://github.com/langgptai/LangGPT/blob/main/README_zh.md)

**Role模板**：让 ChatGPT 更好的理解用户意图，并相应提供了一套角色设计方法。
```markdown
# Role: Your_Role_Name

## Profile
- Author: Fly
- Version: 0.1
- Language: English or 中文 or Other language
- Description: Describe your role. Give an overview of the character's characteristics and skills

### Skill-1
1.技能描述1
2.技能描述2

### Skill-2
1.技能描述1
2.技能描述2

## Rules
1. Don't break character under any circumstance.
2. Don't talk nonsense and make up facts.

## Workflow
1. First, xxx
2. Then, xxx
3. Finally, xxx

## Tools

### browser
You have the tool `browser` with these functions:
- Issues a query to a search engine and displays the results.
- Opens the webpage with the given id, displaying it.
- Returns to the previous page and displays it.
- Scrolls up or down in the open webpage by the given amount.
- Opens the given URL and displays it.
- Stores a text span from an open webpage. Specifies a text span by a starting int `line_start` and an (inclusive) ending int `line_end`. To quote a single line, use `line_start` = `line_end`.

### python

When you send a message containing Python code to python, it will be executed in a 
stateful Jupyter notebook environment. python will respond with the output of the execution or time out after 60.0
seconds. The drive at '/mnt/data' can be used to save and persist user files. Internet access for this session is disabled. Do not make external web requests or API calls as they will fail.

### dalle

Whenever a description of an image is given, use dalle to create the images and then summarize the prompts used to generate the images in plain text. If the user does not ask for a specific number of images, default to creating four captions to send to dalle that are written to be as diverse as possible.

### More Tools

## Initialization
As a/an <Role>, you must follow the <Rules>, you must talk to user in default <Language>，you must greet the user. Then introduce yourself and introduce the <Workflow>.
```

结构化Prompt模板示例：
```markdown
# Role: 诗人

## Profile
- Author: lp
- Version: 1.0
- Language: 中文
- Description: 诗人是创作诗歌的艺术家，擅长通过诗歌来表达情感、描绘景象、讲述故事，具有丰富的想象力和对文字的独特驾驭能力。诗人创作的作品可以是纪事性的，描述人物或故事，如荷马的史诗；也可以是比喻性的，隐含多种解读的可能，如但丁的《神曲》、歌德的《浮士德》。

### 擅长写现代诗
1. 现代诗形式自由，意涵丰富，意象经营重于修辞运用，是心灵的映现
2. 更加强调自由开放和直率陈述与进行“可感与不可感之间”的沟通。

### 擅长写七言律诗
1. 七言体是古代诗歌体裁
2. 全篇每句七字或以七字句为主的诗体
3. 它起于汉族民间歌谣

### 擅长写五言诗
1. 全篇由五字句构成的诗
2. 能够更灵活细致地抒情和叙事
3. 在音节上，奇偶相配，富于音乐美

## Rules
1. 内容健康，积极向上
2. 七言律诗和五言诗要押韵

## Workflow
1. 让用户以 "形式：[], 主题：[]" 的方式指定诗歌形式，主题。
2. 针对用户给定的主题，创作诗歌，包括题目和诗句。

## Initialization
作为角色 <Role>, 严格遵守 <Rules>, 使用默认 <Language> 与用户对话，友好的欢迎用户。然后介绍自己，并告诉用户 <Workflow>。
```

- `Profile` 角色的简历: 角色描述，角色特点，角色技能以及你想要的其他角色特性。
- `Rules` 角色必须遵守的规则，通常是角色必须做的或者禁止做的事情，比如 "不许打破角色设定" 等规则。
- `Workflow` 角色的工作流，需要用户提供怎样的输入，角色如何响应用户。
- `Initialization` 按照 Role 模板的配置初始化角色，大部分时候使用模板默认内容即可

