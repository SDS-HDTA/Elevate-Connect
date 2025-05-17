# 1. Registration and Login Features

## 1.1 Overview

| Feature Module | Endpoint | Method | Description |
| --- | --- | --- | --- |
| User Login | `/login` | POST | Email and password login |
| User Registration | `/register` | POST | Create a new user |
| Send Verification Code | `/password/resetCode` | POST | Send password reset code |
| Update Password | `/password/update` | POST | Reset password via code |

---

## 1.2 User Login

- **URL:** `/login`
- **Method:** POST
- Content-type: `/application/x-www-form-urlencoded`

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

- **URL:** `/register`
- **Method:** POST
- Content-type: `/application/x-www-form-urlencoded`

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

- **URL:** `/password/update`
- **Method:** POST
- Content-type: `/application/x-www-form-urlencoded`

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

## Overview

| API No. | API Name | Method | Path | Main Parameters | Description |
| --- | --- | --- | --- | --- | --- |
| 2.1 | Get User Information | GET | `/user/info` | Query: `userId`; Header: `Authorization` | Retrieve detailed user information by user ID |
| 2.2 | User Logout | POST | `/logout` | Header: `Authorization` | Log out the user and clear server-side session |
| 2.3 | Get My Projects | GET | `/projects/my` | Query: `userId`; Optional: `searchType`, `searchValue`; Header: `Authorization` | Get the list of projects the current user is participating in |
| 2.4 | Get All Projects | GET | `/projects/all` | Query: `page`, `size` (required); Optional: `searchType`, `searchValue`; Header: `Authorization` | Retrieve all public projects in the system |
| 2.5 | Create Project | POST | `/projects/create` | Form data: `name`, `area`, `category`, `description`, `status`; Optional: `image` (file upload) | Create a new project with support for image upload |
| 2.6 | Get Available Projects | GET | `/projects/available` | Query: `searchQuery` (required) | Retrieve projects available for the user to join based on keyword |
| 2.7 | Join Project | POST | `/projects/join` | Form: `projectId`, `userId` | Join a specific project |
| 2.8 | Get Project Detail | GET | `/project/{projectId}` | Path parameter: `projectId` | Retrieve detailed information about a project by project ID |

## 2.1 Get User Information

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
    
    
    | Name | Type | Required | Description |
    | --- | --- | --- | --- |
    | userId | number | Yes | ID of the user |

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

## 2.3 Get My Projects

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
    
    
    | Name | Type | Required | Description |
    | --- | --- | --- | --- |
    | userId | number | Yes | ID of the current user |
    | searchType | number | No | Searching project by name or category or area |
    | searchValue | String | No | Searching value |

**Example Request URL:**

```
/projects/my?userId=1&searchType=0&searchValue=abc

```

### Response

```json
{
  "code": 1,
  "data": [
    {
      "id": 1,
      "name": "AI Assistant Project",
      "creatorId": 23,
      "status": 1,
      "description": "A project to build an AI chatbot.",
      "imageUrl": "<https://example.com/image.png>",
      "channelId": 3,
      "category": "Technology",
      "deadline": "2025-12-31",
      "tags": "AI,Chatbot,ML",
      "createTime": "2025-01-01T10:00:00Z",
      "updateTime": "2025-04-23T12:00:00Z"
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

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| page | number | Yes | Page number, default is 1 |
| size | number | Yes | Items per page, default is 1 |
| searchType | number | No | Searching project by name , category or area |
| searchValue | String | No | Searching value |

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
        "imageUrl": "Project image URL",
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

## 2.5 Create Project

### Interface Description

Used to create a new project, supporting the input of basic project information and image upload.

### Request Information

- **Request URL**: `/projects/create`
- **Request Method**: `POST`
- **Content-Type**: `multipart/form-data`

### Request Parameters

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| name | string | Yes | Project name |
| area | string | Yes | Project region |
| category | string | Yes | Project theme |
| description | string | Yes | Project description |
| status | int | Yes | Project status; accepted values: `planned`, `in-progress`, `completed` |
| image | file | No | Project image, supports `jpg`, `png`, `jpeg` formats |

### Request Example

```jsx
const formData = new FormData()
formData.append('name', 'Example Project')
formData.append('area', 'Beijing')
formData.append('category', 'Architectural Design')
formData.append('description', 'This is an example project description')
formData.append('status', 'planned')
formData.append('image', file) // file is an image file object

```

### Response Parameters

| Parameter | Type | Description |
| --- | --- | --- |
| code | number | Response code, `1` indicates success |
| message | string | Response message |
| data | object | Response data |

### Response Example

```json
{
  "code": 1,
  "message": "Project created successfully",
  "data": {
      "id": 1,
      "name": "AI Assistant Project",
      "creatorId": 23,
      "status": 1,
      "description": "A project to build an AI chatbot.",
      "imageUrl": "<https://example.com/image.png>",
      "channelId": 3,
      "category": "Technology",
      "deadline": "2025-12-31",
      "tags": "AI,Chatbot,ML",
      "createTime": "2025-01-01T10:00:00Z",
      "updateTime": "2025-04-23T12:00:00Z"
  }
}

```

---

## 2.6 Get Available Projects

### **Interface Description**:

Retrieves a list of projects that the user can join based on the provided search criteria.

### Request Information

- Request URL: `/projects/available`
- **Method**: GET

### **Request Parameters**:

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| searchQuery | String | Yes | Search keyword |

### Response Parameters

| Parameter | Type | Description |
| --- | --- | --- |
| code | number | Response code, `1` indicates success |
| message | string | Response message |
| data | object | Response data |

**Error Codes**:

| Code | Description |
| --- | --- |
| 1 | Success |
| 0 | Failure |

### **Response Example**:

```json
{
  "code": 1,                    // Response code: 1 - success, 0 - failure
  "message": "Success",         // Response message
  "data": [                     // List of available projects
    {
      "id": 1,
      "name": "AI Assistant Project",
      "creatorId": 23,
      "status": 1,
      "description": "A project to build an AI chatbot.",
      "imageUrl": "<https://example.com/image.png>",
      "channelId": 3,
      "category": "Technology",
      "deadline": "2025-12-31",
      "tags": "AI,Chatbot,ML",
      "createTime": "2025-01-01T10:00:00Z",
      "updateTime": "2025-04-23T12:00:00Z"
    }
  ]
}

```

---

## 2.7 Join Project

### **Interface Description**:

Allows a user to join a specific project.

### Request Information

- Request URL: `/projects/join`
- **Method**: POST
- Content-type: `/application/x-www-form-urlencoded`

### **Request Parameters**:

| Parameter | Type | Required |
| --- | --- | --- |
| projectId | Integer | Yes |
| userId | Integer | Yes |

### **Response Example**:

```json
{
  "code": 1,              // Response code: 1 - success, 0 - failure
  "message": "Success",   // Response message
  "data": null            // Response data
}

```

**Error Codes**:

| Code | Description |
| --- | --- |
| 1 | Successfully joined the project |
| 0 | Failed to join the project |

**Possible Error Scenarios**:

1. Project not found
2. User is already a member of the project
3. The project has reached the maximum number of members
4. User does not have permission to join the project

**Examples**:

```jsx
// Request Example
POST /projects/join
{
  "projectId": "P001",
  "userId": "U001"
}

// Success Response
{
  "code": 1,
  "message": "Successfully joined the project",
  "data": null
}

// Failure Response
{
  "code": 0,
  "message": "Project not found",
  "data": null
}

```

**Notes**:

1. All requests must include user authentication information in the request headers.
2. Project status is represented by numeric codes:
    - 0: Empathise
    - 1: Discover
    - 2: Define
    - 3: Ideate
    - 4: Prototype
3. Upon successfully joining a project, the user will automatically become a project member with the appropriate permissions.

---

## 2.8 Get Project Details

### **Interface Description**:

Fetch detailed information for a specific project by its ID.

### Request Information:

**Request Method**: `GET`

**Request Path**: `/project/{projectId}`

### Response Parameters

```json
{
  "code": 1,
  "message": "Success",
  "data": {
      "id": 1,
      "name": "AI Assistant Project",
      "creatorId": 23,
      "status": 1,
      "description": "A project to build an AI chatbot.",
      "imageUrl": "<https://example.com/image.png>",
      "channelId": 3,
      "category": "Technology",
      "deadline": "2025-12-31",
      "tags": "AI,Chatbot,ML",
      "createTime": "2025-01-01T10:00:00Z",
      "updateTime": "2025-04-23T12:00:00Z"
  }
}

```

---
