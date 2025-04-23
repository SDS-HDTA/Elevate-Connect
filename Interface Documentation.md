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

---

# 2. Get User Information and Project Data

---

##  2.1 Get User Information

### API Description  
Retrieve detailed information about a user by user ID.

### Request

- **Endpoint**: `/user/info`  
- **Method**: `GET`  
- **Headers**:
  ```
  Authorization: ${token}  // Required, user authentication token
  ```
- **Query Parameters**:
  | Name     | Type   | Required | Description       |
  |----------|--------|----------|-------------------|
  | userId   | number |   Yes   | ID of the user    |

**Example Request URL:**  
```
/user/info?userId=123
```

### Response

```json
{
  "code": 1,
  "data": {
    "id": 123,
    "username": "Username",
    "email": "user@example.com"
  },
  "message": "Successfully retrieved"
}
```

---

## 2.2 User Logout

### API Description

Log out the user and clear the server-side session.


### Request


- **Endpoint**: `/logout`  
- **Method**: `POST`  
- **Headers**:

  ```
  Authorization: ${token}  // Required, user authentication token
  ```

### Response

```json
{
  "code": 1,
  "data": null,
  "message": "Logged out successfully"
}
```

---

##  2.3 Get My Projects

### API Description  
Retrieve a list of projects that the specified user is participating in.

### Request

- **Endpoint**: `/projects/my`  
- **Method**: `GET`  
- **Headers**:
  ```
  Authorization: ${token}  // Required, user authentication token
  ```
- **Query Parameters**:
  | Name     | Type   | Required | Description              |
  |----------|--------|----------|--------------------------|
  | userId   | number |   Yes   | ID of the current user   |

**Example Request URL:**  
```
/projects/my?userId=123
```

### Response

```json
{
  "code": 1,
  "data": [
    {
      "id": 1,
      "name": "AI Assistant Project",
      "creator_id": 23,
      "status": 1,
      "description": "A project to build an AI chatbot.",
      "image_url": "https://example.com/image.png",
      "channel_id": 3,
      "category": "Technology",
      "deadline": "2025-12-31",
      "tags": "AI,Chatbot,ML",
      "create_time": "2025-01-01T10:00:00Z",
      "update_time": "2025-04-23T12:00:00Z"
    }
  ]
}
```

---


## 2.4 Get All Projects

### API Description

Retrieve a list of all public projects in the system.


### Request


- **Endpoint**: `/projects/all`  
- **Method**: `GET`  
- **Headers**:

  ```
  Authorization: ${token}  // Required, user authentication token
  ```

### Query Parameters

| Name   | Type   | Required | Description                  |
| ------ | ------ | -------- | ---------------------------- |
| `page` | number | No       | Page number, default is 1    |
| `size` | number | No       | Items per page, default is 1 |

### Response

```json
{
  "code": 1,
  "data": {
    "total": 100,
    "records": [
      {
        "id": "Project ID",
        "name": "Project name",
        "status": "Project status",
        "category": "Project category",
        "image_url": "Project image URL",
        "createTime": "Creation time",
        "updateTime": "Last updated time",
        "creatorId": 1
      }
    ]
  },
  "message": "Successfully retrieved"
}
```


### Error Codes

- 1: Success  
- 401: Unauthorized or token expired  
- 500: Internal server error

---

