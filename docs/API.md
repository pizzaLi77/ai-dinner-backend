# 晚餐吃啥小助手 API 文档 v2.1

面向 uni-app Vue3 前端联调。默认 Base URL: `http://localhost:8080`。

## 通用约定

除登录和健康检查外，请求头统一携带：

```http
Authorization: Bearer <token>
Content-Type: application/json
```

统一响应：

```json
{
  "code": 0,
  "message": "success",
  "data": {}
}
```

常用错误码：

| code | 含义 |
| --- | --- |
| 0 | 成功 |
| 40001 | 登录已过期或未登录 |
| 40002 | 参数不合法 |
| 40003 | 用户画像不存在 |
| 50001 | 大模型调用失败 |
| 50002 | 大模型输出格式错误 |
| 50003 | 服务暂时不可用 |

## 1. 健康检查

`GET /api/health`

响应 data:

```json
"ok"
```

## 2. 微信登录

`POST /api/auth/wechat/login`

请求：

```json
{
  "code": "wx_login_code",
  "nickname": "可选昵称",
  "avatarUrl": "可选头像"
}
```

响应 data:

```json
{
  "token": "jwt_token",
  "userId": 10001,
  "isNewUser": true,
  "profile": {
    "spicyLevel": 2,
    "preferredTastes": ["家常", "热乎"],
    "dislikedTastes": [],
    "favoriteIngredients": [],
    "dislikedIngredients": [],
    "commonIngredients": ["鸡蛋", "番茄"],
    "preferredCookingTimeMinutes": 20,
    "preferredDifficulty": "easy",
    "cookingTools": ["炒锅"],
    "healthGoals": ["less_oil"],
    "preferenceSummary": "你偏爱家常、热乎、快速完成的晚餐。"
  }
}
```

## 3. 个人画像

`GET /api/profile/me`

响应 data: `UserProfile`。

`PUT /api/profile/me`

请求：

```json
{
  "spicyLevel": 2,
  "preferredTastes": ["家常", "热乎", "少油"],
  "dislikedTastes": ["太甜"],
  "favoriteIngredients": ["鸡蛋"],
  "dislikedIngredients": ["香菜", "动物内脏"],
  "commonIngredients": ["鸡蛋", "番茄", "土豆", "青椒", "挂面"],
  "preferredCookingTimeMinutes": 20,
  "preferredDifficulty": "easy",
  "cookingTools": ["炒锅", "电饭煲"],
  "healthGoals": ["less_oil", "high_protein", "more_vegetables"]
}
```

响应 data: 更新后的 `UserProfile`。

## 4. 生成晚餐推荐

`POST /api/recommendations/generate`

请求：

```json
{
  "freeText": "冰箱有鸡蛋、番茄、青椒，今天下班辛苦了，想要不太麻烦又舒服的餐。",
  "selectedMoods": ["下班很累"],
  "selectedTastes": ["家常", "酸甜", "热乎"],
  "selectedTime": "20 分钟",
  "selectedTools": ["炒锅", "电饭煲"]
}
```

响应 data:

```json
{
  "sessionId": 90001,
  "profileSummary": "你偏爱家常、热乎、少油、能快速做完的晚餐。",
  "recommendations": [
    {
      "id": 80001,
      "sessionId": 90001,
      "type": "easy",
      "typeLabel": "最省事",
      "name": "番茄鸡蛋面",
      "reason": "酸甜开胃，10 分钟就能搞定。",
      "estimatedTimeMinutes": 10,
      "difficulty": "easy",
      "ingredientsUsed": ["番茄", "鸡蛋", "青椒", "面条"],
      "missingIngredients": [{"name": "葱", "optional": true}],
      "steps": ["炒蛋盛出", "番茄炒出汁", "加水煮面", "加蛋和调味"],
      "substitutions": ["青椒可换成小白菜，面条可换成米线。"],
      "tags": ["快手", "开胃", "酸甜", "一锅到底"],
      "coverImageUrl": null,
      "caution": "",
      "feedbackSummary": {
        "liked": false,
        "disliked": false,
        "saved": false,
        "cooked": false,
        "neutral": false,
        "addToToday": false,
        "tooHard": false,
        "tooLight": false,
        "tooOily": false
      }
    }
  ]
}
```

字段枚举：

| 字段 | 可选值 |
| --- | --- |
| `type` | `easy` / `satisfying` / `healthy` |
| `typeLabel` | `最省事` / `最满足` / `最健康` |
| `difficulty` | `easy` / `medium` / `hard` |

## 5. 推荐反馈

`POST /api/recommendations/{recommendationId}/feedback`

请求：

```json
{
  "sessionId": 90001,
  "action": "like",
  "extraReason": "可选：太麻烦 / 太清淡 / 太油"
}
```

`action` 枚举：

`like` / `neutral` / `replace` / `dislike` / `too_hard` / `too_light` / `too_oily` / `save` / `unsave` / `cooked` / `add_to_today`

响应 data:

```json
{
  "success": true,
  "profileSummary": "更新后的画像摘要"
}
```

## 6. 单卡替换

`POST /api/recommendations/{recommendationId}/replace`

请求：

```json
{
  "sessionId": 90001,
  "type": "easy"
}
```

响应 data: 新的 `DinnerRecommendation`。

## 7. 收藏

`POST /api/recommendations/{recommendationId}/favorite`

响应 data: `Favorite`。

`GET /api/favorites?tag=快手&page=1&pageSize=20`

响应 data:

```json
{
  "items": [
    {
      "id": 1,
      "recommendationId": 80001,
      "sourceSessionId": 90001,
      "name": "番茄鸡蛋面",
      "summary": "酸甜开胃，10 分钟就能搞定。",
      "tags": ["快手", "酸甜"],
      "estimatedTimeMinutes": 10,
      "ingredientsUsed": ["番茄", "鸡蛋"],
      "steps": ["炒蛋盛出", "番茄炒出汁"],
      "coverImageUrl": null,
      "createdAt": "2026-05-16T23:00:00"
    }
  ],
  "page": 1,
  "pageSize": 20,
  "total": 1
}
```

`DELETE /api/favorites/{favoriteId}`

响应 data: `true`。

`POST /api/favorites/{favoriteId}/similar`

响应 data: `GenerateDinnerResponse`。

`POST /api/favorites/{favoriteId}/add-to-today`

响应 data: `TodayDinnerPlanItem`。

## 8. 今日晚餐

`GET /api/dinner-plans/today/items`

响应 data:

```json
[
  {
    "id": 1,
    "planDate": "2026-05-16",
    "sourceType": "favorite",
    "sourceId": 1,
    "dishName": "番茄鸡蛋面",
    "tags": ["快手"],
    "status": "planned",
    "createdAt": "2026-05-16T23:00:00",
    "updatedAt": "2026-05-16T23:00:00"
  }
]
```

`POST /api/dinner-plans/today/items`

请求：

```json
{
  "sourceType": "favorite",
  "sourceId": 1,
  "dishName": "番茄鸡蛋面"
}
```

响应 data: `TodayDinnerPlanItem`。

## 9. 历史

`GET /api/history?page=1&pageSize=10`

响应 data: 分页的历史 session 列表，每条包含原始输入和推荐卡片。

`GET /api/history/grouped?page=1&pageSize=10`

响应 data:

```json
{
  "groups": [
    {
      "dateLabel": "2026-05-16",
      "items": [
        {
          "sessionId": 90001,
          "inputSummary": "冰箱有鸡蛋、番茄、青椒...",
          "recommendationSummary": "番茄鸡蛋面 + 番茄鸡蛋盖饭 + 豆腐蛋花汤",
          "feedbackSummary": "liked 番茄鸡蛋面"
        }
      ]
    }
  ],
  "page": 1,
  "pageSize": 10,
  "total": 1
}
```

`POST /api/history/{sessionId}/similar`

响应 data: `GenerateDinnerResponse`。

## 10. 埋点

`POST /api/analytics/events`

请求：

```json
{
  "eventName": "home_generate_click",
  "properties": {
    "source": "home"
  }
}
```

响应 data: `true`。

## 11. 前端类型摘要

```ts
export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

export interface GenerateDinnerRequest {
  freeText: string
  selectedMoods: string[]
  selectedTastes: string[]
  selectedTime: string | null
  selectedTools: string[]
}

export interface DinnerRecommendation {
  id: number
  sessionId: number
  type: 'easy' | 'satisfying' | 'healthy'
  typeLabel: '最省事' | '最满足' | '最健康'
  name: string
  reason: string
  estimatedTimeMinutes: number
  difficulty: 'easy' | 'medium' | 'hard'
  ingredientsUsed: string[]
  missingIngredients: Array<{ name: string; optional: boolean }>
  steps: string[]
  substitutions: string[]
  tags: string[]
  coverImageUrl?: string | null
  caution?: string
}
```
