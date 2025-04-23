# 1. Registration and Login Features

## 1.1 Overview

| Feature Module          | Endpoint                   | Method   | Description                     |
|-------------------------|----------------------------|----------|---------------------------------|
| User Login              | `/login`                   | POST     | Email and password login        |
| User Registration       | `/register`                | POST     | Create a new user               |
| Send Verification Code  | `/password/resetCode`      | POST     | Send password reset code        |
| Update Password         | `/password/update`         | POST     | Reset password via code         |

---

## 1.2 User Login

**URL:** `/login`  
**Method:** POST  

### Request Parameters  
```json
{
  "email": "user@example.com",
  "password": "yourPassword123"
}
```

### Response Examples  
**Success:**  
```json
{
  "code": 1,
  "message": "Login Successful",
  "data": {
    "id": 123,
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
  }
}
```

**Error:**  
```json
{
  "code": 0,
  "message": "Email or password is incorrect"
}
```

---

## 1.3 User Registration

**URL:** `/register`  
**Method:** POST  

### Request Parameters  
```json
{
  "email": "newuser@example.com",
  "password": "SecurePass123!",
  "invitedCode": "REF12345"
}
```

### Parameter Requirements  
- Password: 8-20 characters, must include uppercase, lowercase, and numbers  
- Invitation Code: 6-12 alphanumeric characters (field name: `invitedCode`)  

### Response Examples  
**Success:**  
```json
{
  "code": 1,
  "message": "Registration Successful",
  "data": {
    "id": 124,
    "email": "newuser@example.com"
  }
}
```

**Error:**  
```json
{
  "code": 0,
  "message": "Invitation code does not exist"
}
```

---

## 1.4 Send Reset Verification Code

**URL:** `/password/resetCode`  
**Method:** POST  

### Request Parameters  
```json
{
  "email": "user@example.com"
}
```

### Response Example  
```json
{
  "code": 1,
  "message": "Verification code sent"
}
```

---

## 1.5 Update Password

**URL:** `/password/update`  
**Method:** POST  

### Request Parameters  
```json
{
  "email": "user@example.com",
  "verificationCode": "A1B2C3",
  "newPassword": "NewPass123!"
}
```

### Response Examples  
**Success:**  
```json
{
  "code": 1,
  "message": "Password updated successfully"
}
```

**Error:**  
```json
{
  "code": 0,
  "message": "Invalid or expired verification code"
}
```

# 2. 获取用户信息和项目数据

## 2.1获得用户信息

### 接口描述

获取当前登录用户的详细信息

### 请求信息

- **接口地址**: `/user/info`
- **请求方式**: GET
- **请求头**:
  
    ```
    Authorization: ${token}  // 必填，用户登录凭证
    
    ```
    

### 响应信息

```json
{
  "code": 1,
  "data": {
    "id": "用户ID",
    "username": "用户名",
    "email": "用户邮箱",
    "type": "用户角色"
  },
  "message": "获取成功"
}

```

### 错误码

- 200: 成功
- 401: 未登录或token失效
- 500: 服务器内部错误

## 2.2 用户登出

### 接口描述

用户退出登录，清除服务器端会话

### 请求信息

- **接口地址**: `/logout`
- **请求方式**: POST
- **请求头**:
  
    ```
    Authorization: ${token}  // 必填，用户登录凭证
    
    ```
    

### 响应信息

```json
{
  "code": 1,
  "data": null,
  "message": "退出成功"
}

```

### 错误码

- 200: 成功
- 401: 未登录或token失效
- 500: 服务器内部错误

## 2.3 获取我的项目列表

### 接口描述

获取当前用户参与的项目列表

### 请求信息

- **接口地址**: `/projects/my`
- **请求方式**: GET
- **请求头**:
  
    ```
    Authorization: ${token}  // 必填，用户登录凭证
    
    ```
    

### 响应信息

```json
{
  "code": 1,
  "data": [
    {
      "id": "项目ID",
      "title": "项目标题",
      "state": "项目状态",  
      "area": "项目地区",
      "subject": "项目主题",
      "image": "项目图片URL",
      "createTime": "创建时间",
      "updateTime": "更新时间"
    }
  ],
  "message": "获取成功"
}

```

### 错误码

- 200: 成功
- 401: 未登录或token失效
- 500: 服务器内部错误

## 2.4 获取所有项目列表

### 接口描述

获取系统中所有公开的项目列表

### 请求信息

- **接口地址**: `/projects/all`
- **请求方式**: GET
- **请求头**:
  
    ```
    Authorization: ${token}  // 必填，用户登录凭证
    
    ```
    

### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| --- | --- | --- | --- |
| page | number | 否 | 页码，默认1 |
| size | number | 否 | 每页数量，默认10 |
| keyword | string | 否 | 搜索关键词 |

### 响应信息

```json
{
  "code": 1,
  "data": {
    "total": "总记录数",
    "pages": "总页数",
    "current": "当前页码",
    "records": [
      {
        "id": "项目ID",
        "title": "项目标题",
        "state": "项目状态",  // 已完成/进行中/计划中
        "area": "项目地区",
        "subject": "项目主题",
        "image": "项目图片URL",
        "createTime": "创建时间",
        "updateTime": "更新时间",
        "creator": {
          "id": "创建者ID",
          "username": "创建者用户名"
        }
      }
    ]
  },
  "message": "获取成功"
}

```
