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
| 2.6 | Get Project by Name | GET | `/projects/searchByName` | Query: `searchQuery` (required) | Retrieve projects available for the user to join based on keyword |
| 2.7 | Join Project | POST | `/projects/join` | Form: `projectId`, `userId` | Join a specific project |
| 2.8 | Get Project Detail | GET | `/projects/{projectId}` | Path parameter: `projectId` | Retrieve detailed information about a project by project ID |

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
| deadline | string | Yes | Project’s deadline |
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

## 2.6 Get Project By Name

### **Interface Description**:

Retrieves a list of projects that the user can join based on the provided search criteria.

### Request Information

- Request URL: `/projects/searchByName`
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

**Request Path**: `/projects/{projectId}`

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
  },
   "members": [
      {
        "userId": "123",
        "username": "john_doe",
        "email": "john@example.com",
        "type": "Developer",
        "isOwner": false
      }
    ],
  "creatorName": "fz"
}

```

---

# 3. Project Management

---

## 3.1 Remove Project Member

### **Interface Description**:

Removes a member from the specified project. Only the project owner has the permission to perform this operation.

### Request Information

- **Request URL**: `projects/{projectId}/members/{userId}`
- **Method**: DELETE
- **Headers**:
    - `Authorization: {token}`

### **Request Parameters**:

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| projectId | number | Yes | Unique identifier of the project |
| memberId | number | Yes | Unique identifier of the member to be removed |
| userId | number | Yes | To verify whether the user has right to remove  |

### Response Parameters

| Parameter | Type | Description |
| --- | --- | --- |
| code | number | Response status code |
| message | string | Response message |

### **Response Example**:

```json
{
  "code": 1,
  "message": "Member removed successfully"
}

```

---

## 3.2 Leave Project

### **Interface Description**:

Allows a user to leave a project. Once the request is processed, the user will no longer be a member of the specified project.

### Request Information

- **Request URL**: `/projects/leave`
- **Method**: POST
- **Content-Type**: `application/x-www-form-urlencoded`

### **Request Parameters**:

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| projectId | number | Yes | The ID of the project |
| userId | number | Yes | The ID of the user leaving |

### **Request Example**:

```
POST /api/projects/leave
Content-Type: application/x-www-form-urlencoded

projectId=123&userId=456

```

### Response Parameters

| Parameter | Type | Description |
| --- | --- | --- |
| code | number | Response code: `1` for success, `0` for failure |
| message | string | Response message |
| data | object | Response data (null for this operation) |

### **Response Example**:

```json
{
  "code": 1,
  "message": "Successfully left the project",
  "data": null
}

```

```json
{
  "code": 0,
  "message": "Failed to leave project",
  "data": null
}

```

### **Error Codes**:

| Code | Description |
| --- | --- |
| 0 | Operation failed |
| 1 | Operation succeeded |

---

## 3.3 Dismiss Project

### **Interface Description**:

Allows the project owner to permanently dismiss a project. This operation is irreversible and will remove the project for all members.

### Request Information

- **Request URL**: `/projects/{projectId}/dismiss`
- **Method**: DELETE
- **Content-Type**: `application/json`
- **Headers**:
    - `Authorization: <token>`

### **Path Parameters**:

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| projectId | number | Yes | The ID of the project |
| userId | number  | Yes  | To check the right |

### **Request Example**:

```
DELETE /api/projects/123/dismiss
Authorization: <token>

```

### Response Parameters

| Parameter | Type | Description |
| --- | --- | --- |
| code | number | Response code: `1` for success, `0` for failure |
| message | string | Response message |
| data | object | Response data (null for this operation) |

### **Response Example**:

```json
{
  "code": 1,
  "message": "Project has been successfully dismissed",
  "data": null
}

```

```json
{
  "code": 0,
  "message": "Failed to dismiss project",
  "data": null
}

```

以下是你修改后的接口文档，我已经将其全面转换为标准的英文 RESTful API 文档格式，并保持你指定的内容结构和格式说明：

---

## **3.4 Get All Posts**

### **Interface Description**

Retrieves all posts and related messages within a project's discussion channel.

### **Request Information**

- **Request URL**: `/projects/{channelId}/posts`
- **Method**: GET

### **Request Parameters**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| channelId | int | Yes | Unique ID of project (path parameter) |

### **Response Example**

```json
{
  "code": 1,
  "data": [
    {
      "id": 1,
      "title": "Introduction",
      "description": "Let's start with team roles.",
      "creatorName": "Alice",
      "createTime": "2025-05-01 10:00:00",
      "userId": 3,
      "replies": [
        {
          "id": 101,
          "content": "I'll handle frontend.",
          "senderName": "Bob",
          "createTime": "2025-05-01 10:10:00",
          "userId": "user456"
        }
      ]
    }
  ]
}

```

## **3.5 Get Channel**

### **Interface Description**

To get a channel ID

### **Request Information**

- **Request URL**: `/projects/{projectId}/channel`
- **Method**: GET

### **Request Parameter**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| projectId | int | Yes | Unique ID of project (path parameter) |

### **Response Example**

```json
{
  "code": 1,
  "data": [
    {
			"id": 1,
      "projectId": 1,
      "description": "Let's start with team roles.",
      "lastPostTime": "2025-05-01 10:00:00",
      "undateTime": "2025-05-01 10:00:00",
      "title": "TEST2"
      "totalPosts": 4
      "createTime": "2025-05-23T06:25:12.000+00:00"
      "description": null
      
    }
  ]
  "message" : "Success"
}

```

## **3.6 Create a New Post**

### **Interface Description**

Creates a new post in the specified project channel.

### **Request Information**

- **Request URL**: `/projects/{projectId}/channel/post`
- **Method**: POST
- **Content-Type**: `application/x-www-form-urlencoded`

### **Request Parameters**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| channelId | int | Yes | Project ID (path parameter) |
| title | string | Yes | Title of the post |
| description | string | Yes | Description or content of the post |
| createTime | string | Yes | Timestamp in `YYYY-MM-DD HH:mm:ss` format |
| userId | int | Yes | ID of the user creating the post |

### **Response Example**

```json
{
  "code": 1,
  "data": {
    "id": "2",
    "title": "Meeting Schedule",
    "description": "Weekly sync every Friday.",
    "creatorName": "John",
    "createTime": "2025-05-18 09:00:00",
    "userId": "89",
    "replies" : []
  }
    "message": ["Success"]
}

```

---

## **3.7 Reply to a Post**

### **Interface Description**

Sends a reply message to a specific post within the project channel.

### **Request Information**

- **Request URL**: `/projects/{projectId}/channel/reply`
- **Method**: POST
- **Content-Type**: `application/x-www-form-urlencoded`

### **Request Parameters**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| postId | int | Yes | ID of the post to reply to |
| content | string | Yes | Reply content |
| createTime | string | Yes | Timestamp in `YYYY-MM-DD HH:mm:ss` format |
| userId | int | Yes | ID of the user sending the reply |
| channelId | int  | Yes | ID of channel |

### **Response Example**

```json
{
  "code": 1,
  "data": {
	  "postId": 1,
    "id": 102,
    "content": "Got it, see you Friday.",
    "senderName": "Emma",
    "createTime": "2025-05-18 09:15:00",
    "userId": 321
  }
  "message": "Success"
}

```

---

## 3.8 WebSocket Communication

---

### **Connection Information**

- **WebSocket URL**: `ws://localhost:8080/projects/{projectId}/channel`
- **Path Parameter**:
    - `projectId`: The ID of the project to join the channel

---

### **Message Types**

### **1. New Message Notification**

```json
{
  "type": "new_message",
  "data": {
    "id": 23,
    "content": "New update here.",
    "senderName": "Olivia",
    "createTime": "2025-05-18 10:00:00",
    "userId": 3
  }
}

```

### **2. New Post Notification**

```json
{
  "type": "new_post",
  "data": {
    "id": 23,
    "title": "New Task Assigned",
    "description": "Please complete by next week.",
    "creatorName": "Leo",
    "createTime": "2025-05-18 10:30:00",
    "userId": 10,
    "messages": []
  }
}

```

### **3. Delete Message Notification**

```json
{
  "type": "delete_message",
  "postId": 23,
  "messageId": 23
}

```

### **4. Delete Post Notification**

```json
{
  "type": "delete_post",
  "postId": 123
}

```

---

## Error Handling

### **HTTP Error Response**

```json
{
  "code": 0,
  "message": "An error occurred"
}

```

### **WebSocket Error Handling**

- Errors are logged in the browser console.
- Users will be shown an alert or error message when a WebSocket error occurs.

---

### Notes

1. All timestamps must follow the format: `YYYY-MM-DD HH:mm:ss`.
2. WebSocket automatically listens for real-time events after successful connection.
3. Ensure WebSocket connection state is `OPEN` before sending messages.
4. All APIs require the user to be logged in.

---

## 3.9 Get Project Status

### **Interface Description**

Retrieves the current progress status of a specific project.

### **Request Information**

- **Request URL**: `/projects/{projectId}/status`
- **Method**: GET

### **Request Parameters**

| Parameter | Type | In | Required | Description |
| --- | --- | --- | --- | --- |
| projectId | number | path | Yes | ID of the project |

### **Response Parameters**

| Parameter | Type | Description |
| --- | --- | --- |
| code | number | Response code: 1 - success, 0 - failure |
| message | string | Response message |
| data | object | Response data object |
| data.project | object | Project status object |
| data.project.status | number | Project stage (0–5, see below) |

### **Status Mapping**

| Value | Stage |
| --- | --- |
| 0 | Empathise |
| 1 | Discover |
| 2 | Define |
| 3 | Ideate |
| 4 | Prototype |
| 5 | Feedback |

### **Response Example**

```json
{
  "code": 1,
  "message": "success",
  "data": {
     "status": 2   
  }
}

```

---

## 3.10 Update Project Status

### **Interface Description**

Updates the current status (stage) of a project. Only the project owner is allowed to perform this operation.

### **Request Information**

- **Request URL**: `/projects/{projectId}/status`
- **Method**: PUT
- **Content-Type**: `application/json`
- **Authorization Header**: Required

### **Request Parameters**

| Parameter | Type | In | Required | Description |
| --- | --- | --- | --- | --- |
| projectId | number | path | Yes | ID of the project |
| status | number | body | Yes | New project status (0–5) |
| userId | number | body | Yes | check the rights |

**Request Body Example**:

```json
{
  "status": 3
}

```

### **Response Parameters**

| Parameter | Type | Description |
| --- | --- | --- |
| code | number | Response code: 1 - success, 0 - failure |
| message | string | Response message |
| data | object | Always null for this endpoint |

### **Response Example**

```json
{
  "code": 1,
  "message": "success",
  "data": null
}

```

---

## 3.11 Get Iterations

### **Interface Description**

Retrieves the iteration data for a specific project and stage.

### **Request Information**

- **Request URL**: `/projects/{projectId}/iterations`
- **Method**: GET

### **Request Parameters**

| Parameter | Type | In | Required | Description |
| --- | --- | --- | --- | --- |
| projectId | number | path | Yes | ID of the project |
| status | number | query | Yes | Current project status (stage code) |

### **Response Parameters**

| Parameter | Type | Description |
| --- | --- | --- |
| code | number | Response code: 1 - success, 0 - failure |
| data | array | List of iterations |
| data[].id | number | Iteration ID |
| data[].name | string | Iteration name |
| data[].tasks | array | List of tasks in the iteration |
| data[].tasks[].id | number | Task ID |
| data[].tasks[].code | string | Task code |
| data[].tasks[].creator | string | Creator name |
| data[].tasks[].creatorId | string | Creator ID |
| data[].tasks[].content | string | Task content |
| data[].tasks[].status | string | Task status |
| data[].tasks[].assignee | string | Assigned user |
| data[].tasks[].children | array | Subtasks (if any) |

### **Response Example**

```json
{
  "code": 1,
  "data": [
    {
      "id": 101,
      "title": "Iteration-1",
      "tasks": [
        {
          "id": 501,
          "code": "TASK-001",
          "creator": "Alice",
          "creatorId": "user123",
          "content": "Design homepage",
          "status": "In Progress",
          "assignee": "Bob",
          "children": [
            {
              "id": 502,
              "code": "TASK-001-1",
              "creator": "Alice",
              "creatorId": 12,
              "content": "Design header",
              "status": 1,
              "assignee": 2
            }
          ]
        }
      ]
    }
  ]
}

```

---

## 3.12 Delete Task

### **Interface Description**

Deletes a task from the specified project. Only authorized users may perform this operation.

### **Request Information**

- **Request URL**: `/projects/{projectId}/tasks/{taskId}`
- **Method**: DELETE

### **Request Parameters**

| Parameter | Type | In | Required | Description |
| --- | --- | --- | --- | --- |
| projectId | number | path | Yes | ID of the project |
| taskId | number | path | Yes | ID of the task to delete |
| userId | number | body | Yes | Check the rights |

### **Response Parameters**

| Parameter | Type | Description |
| --- | --- | --- |
| code | number | Response code: 1 - success, 0 - failure |
| message | string | Response message |

### **Response Example**

```json
{
  "code": 1,
  "message": "Task deleted successfully"
}

```

### **Error Response Example**

```json
{
  "code": 0,
  "message": "Task deletion failed"
}

```

---

### **Permission Rules**

- Only the **project creator** may delete any task.
- The **task creator** may delete tasks they created.
- Other users are **not permitted** to delete tasks.

---

### **Notes**

1. Deleting a parent task will also delete all its child tasks.
2. Frontend should validate permissions before attempting deletion.
3. A confirmation prompt is recommended before deletion.
4. On deletion failure, frontend should rollback any local changes.

---

## 3.13 Create Task

### **Interface Description**

Creates a new task within a specified iteration.

### **Request Information**

- **Request URL**: `/projects/{projectId}/tasks`
- **Method**: POST
- **Content-Type**: `application/json`
- **Authorization**: Required

### **Request Parameters**

| Field | Type | Required | Description |
| --- | --- | --- | --- |
| projectId | string | Yes (path) | Project ID |
| code | string | Yes | Task code or title |
| creatorId | string | Yes | ID of the creator |
| content | string | Yes | Task description |
| status | number | Yes | Task status: `"TODO"`, `"IN PROGRESS"`, `"DONE"` |
| assignee | number | Yes | ID of the assignee |
| createTime | string | Yes | Timestamp in `YYYY-MM-DD HH:mm:ss` format |
| taskId | number | Yes | Determine whether it’s a subtask or not |
| iterationId | number | Yes | The new task belong to which iteration |

### **Example Request Body:**

```json
{
  "taskId": 3,
  "code": "New Task",
  "creator": "Zhang San",
  "creatorId": 1,
  "content": "Task description",
  "status": 0,
  "assignee": 1,
  "createTime": "2024-03-21 10:00:00"
}

```

### **Response Example**

```json
{
  "code": 1,
  "message": "success",
  "data": {
    "id": 1,
    "type": "task",
    "code": "New Task",
    "creator": "Zhang San",
    "creatorId": "user_001",
    "content": "Task description",
    "status": "TODO",
    "assignee": "user_001",
    "createTime": "2024-03-21T10:00:00"
  }
}

```

### Note:

`"taskId" : 0` means that it doesn’t have a parent task. 

## 3.14 Update Task

### **Interface Description**

Updates details of an existing task. Supports partial updates.

### **Request Information**

- **Request URL**: `/projects/{projectId}/tasks/{taskId}`
- **Method**: PUT
- **Content-Type**: `application/json`
- **Authorization**: Required

### **Request Parameters**

| Field | Type | Required | Description |
| --- | --- | --- | --- |
| projectId | string | Yes (path) | Project ID |
| taskId | number | Yes (path) | Task ID |
| code | string | No | Updated task code |
| content | string | No | Updated task content |
| status | number | No | 0,1,2 |
| assignee | string | No | Updated assignee ID |
| userId | number | Yes | check the rights |

### **Example Request Body:**

```json
{
  "code": "Updated Code",
  "content": "Updated task content",
  "status": 0,
  "assignee": 2
}

```

### **Response Example**

```json
{
  "code": 1,
  "message": "success",
  "data": {
    "id": 1,
    "type": "task",
    "code": "Updated Code",
    "creator": "Zhang San",
    "creatorId": "user_001",
    "content": "Updated task content",
    "status": "IN PROGRESS",
    "assignee": "user_002",
    "createTime": "2024-03-21T10:00:00"
  }
}

```

---

## Common Information

### **Status Values**

| Status | Description |
| --- | --- |
| 0 | To do |
| 1 | In progress |
| 2 | Completed |

### **Error Codes**

| Code | Description |
| --- | --- |
| 1 | Success |
| 0 | Failure |

## 3.15 Create New Iteration

### **Interface Description**

Creates a new iteration for a specified project, corresponding to one of the six design thinking phases.

---

### **Request Information**

- **Request URL**: `/projects/{projectId}/iterations`
- **Method**: POST
- **Content-Type**: `application/json`
- **Authorization**: Required

---

### **Path Parameters**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| projectId | number | Yes | Unique project ID |
| userId | number | Yes | check the rights |

---

### **Request Body Parameters**

| Field | Type | Required | Description |
| --- | --- | --- | --- |
| status | number | Yes | Iteration status (0–5, see status mapping) |

**Example Request Body:**

```json
{
  "status": 0
}

```

---

### **Status Mapping**

| Status Code | Design Phase |
| --- | --- |
| 0 | Empathise |
| 1 | Discover |
| 2 | Define |
| 3 | Ideate |
| 4 | Prototype |
| 5 | Feedback |

---

### **Example Success Response:**

```json
{
  "code": 1,
  "message": "success",
  "data": {
    "id": 1,
    "title": "Iteration-1",
    "status": 0,
    "projectId": 1,
    "createTime": "2024-03-21 10:00:00",
    "tasks": []
  }
}

```

### **Example Failure Response:**

```json
{
  "code": 0,
  "message": "Failed to create iteration"
}

```

---

### **Permissions**

- User must be authenticated.

---

### **Notes**

1. Only one iteration per design phase (`status`) is allowed for each project.
2. Creating an iteration will automatically update the project's status to match the iteration.
3. The title is auto-generated in the format `"Iteration-{index}"`.
4. Creation time is automatically assigned by the backend.
