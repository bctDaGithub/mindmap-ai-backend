# 📁 Workspace API Testing Guide

## Tổng quan
Workspace là nơi tổ chức và quản lý các mindmap theo nhóm hoặc dự án. Mỗi workspace có thể chứa nhiều mindmap.

---

## 🔗 Endpoints

Base URL: `http://localhost:8080/api/v1/workspaces`

---

## 📝 CRUD Operations

### 1️⃣ Tạo Workspace mới

**Request:**
```http
POST http://localhost:8080/api/v1/workspaces?ownerId=1
Content-Type: application/json

{
  "name": "Product Development Team",
  "description": "Workspace for product development and planning"
}
```

**Response: 201 Created**
```json
{
  "workspaceId": 1,
  "name": "Product Development Team",
  "description": "Workspace for product development and planning",
  "ownerId": 1,
  "createdAt": "2025-10-08T10:00:00.000+00:00"
}
```

**Validation:**
- `name`: Bắt buộc, 3-100 ký tự
- `description`: Tùy chọn

---

### 2️⃣ Lấy tất cả Workspaces

**Request:**
```http
GET http://localhost:8080/api/v1/workspaces
```

**Response: 200 OK**
```json
[
  {
    "workspaceId": 1,
    "name": "Product Development Team",
    "description": "Workspace for product development and planning",
    "ownerId": 1,
    "createdAt": "2025-10-08T10:00:00.000+00:00"
  },
  {
    "workspaceId": 2,
    "name": "Marketing Team",
    "description": "Workspace for marketing campaigns",
    "ownerId": 2,
    "createdAt": "2025-10-08T11:00:00.000+00:00"
  }
]
```

---

### 3️⃣ Lấy Workspace theo ID

**Request:**
```http
GET http://localhost:8080/api/v1/workspaces/1
```

**Response: 200 OK**
```json
{
  "workspaceId": 1,
  "name": "Product Development Team",
  "description": "Workspace for product development and planning",
  "ownerId": 1,
  "createdAt": "2025-10-08T10:00:00.000+00:00"
}
```

**Response: 404 Not Found** (nếu không tìm thấy)

---

### 4️⃣ Lấy Workspaces theo Owner

**Request:**
```http
GET http://localhost:8080/api/v1/workspaces/owner/1
```

**Response: 200 OK**
```json
[
  {
    "workspaceId": 1,
    "name": "Product Development Team",
    "description": "Workspace for product development and planning",
    "ownerId": 1,
    "createdAt": "2025-10-08T10:00:00.000+00:00"
  },
  {
    "workspaceId": 3,
    "name": "Personal Projects",
    "description": "My personal workspace",
    "ownerId": 1,
    "createdAt": "2025-10-08T12:00:00.000+00:00"
  }
]
```

---

### 5️⃣ Cập nhật Workspace

**Request:**
```http
PUT http://localhost:8080/api/v1/workspaces/1?userId=1
Content-Type: application/json

{
  "name": "Product Development & Design Team",
  "description": "Updated workspace for product development, design and planning"
}
```

**Response: 200 OK**
```json
{
  "workspaceId": 1,
  "name": "Product Development & Design Team",
  "description": "Updated workspace for product development, design and planning",
  "ownerId": 1,
  "createdAt": "2025-10-08T10:00:00.000+00:00"
}
```

**Partial Update:**
Có thể cập nhật một trong hai hoặc cả hai field:
```json
{
  "name": "New Name Only"
}
```
hoặc
```json
{
  "description": "New Description Only"
}
```

---

### 6️⃣ Xóa Workspace

**Request:**
```http
DELETE http://localhost:8080/api/v1/workspaces/1?userId=1
```

**Response: 204 No Content**

---

## 🔄 Workflow: Tạo Workspace với Mindmaps

### Scenario: Tổ chức dự án với Workspace

#### Bước 1: Tạo Workspace
```bash
POST /api/v1/workspaces?ownerId=1
Body: {
  "name": "Q4 2025 Projects",
  "description": "All projects for Q4 2025"
}
# Nhận workspaceId = 1
```

#### Bước 2: Tạo Mindmap trong Workspace
```bash
POST /api/mindmap?ownerId=1
Body: {
  "title": "Website Redesign",
  "description": "Planning for website redesign project",
  "workspaceId": 1,
  "isPublic": false
}
# Nhận mindMapId = 1
```

#### Bước 3: Tạo thêm Mindmaps khác
```bash
POST /api/mindmap?ownerId=1
Body: {
  "title": "Mobile App Launch",
  "description": "Planning for mobile app launch",
  "workspaceId": 1,
  "isPublic": false
}
# Nhận mindMapId = 2
```

#### Bước 4: Lấy tất cả Mindmaps trong Workspace
```bash
GET /api/mindmap/workspace/1
```

**Response:**
```json
[
  {
    "mindMapId": 1,
    "title": "Website Redesign",
    "description": "Planning for website redesign project",
    "workspaceId": 1,
    "ownerId": 1,
    "isPublic": false,
    "createdAt": "2025-10-08T10:00:00.000+00:00",
    "updatedAt": "2025-10-08T10:00:00.000+00:00"
  },
  {
    "mindMapId": 2,
    "title": "Mobile App Launch",
    "description": "Planning for mobile app launch",
    "workspaceId": 1,
    "ownerId": 1,
    "isPublic": false,
    "createdAt": "2025-10-08T10:05:00.000+00:00",
    "updatedAt": "2025-10-08T10:05:00.000+00:00"
  }
]
```

---

## 📊 Sample Test Data

Dùng script SQL này để tạo test data:

```sql
-- Insert sample workspaces
INSERT INTO workspaces (workspaceid, name, description, ownerid, createdat)
VALUES 
(1, 'Product Team', 'Product development workspace', 1, NOW()),
(2, 'Marketing Team', 'Marketing campaigns workspace', 1, NOW()),
(3, 'Personal', 'Personal projects', 2, NOW());

-- Insert mindmaps in workspaces
INSERT INTO mindmaps (mind_map_id, title, description, workspace_id, owner_id, is_public, created_at, updated_at)
VALUES 
(1, 'Q4 Roadmap', 'Product roadmap for Q4', 1, 1, false, NOW(), NOW()),
(2, 'Feature Ideas', 'New feature brainstorming', 1, 1, false, NOW(), NOW()),
(3, 'Campaign Plan', 'Marketing campaign planning', 2, 1, false, NOW(), NOW());
```

---

## 🧪 Test Cases với Postman/Thunder Client

### Collection Structure:

**Folder: Workspace CRUD**

1. **Create Workspace**
   - Method: POST
   - URL: `{{baseUrl}}/api/v1/workspaces?ownerId=1`
   - Body: Raw JSON
   ```json
   {
     "name": "Test Workspace",
     "description": "Testing workspace creation"
   }
   ```

2. **Get All Workspaces**
   - Method: GET
   - URL: `{{baseUrl}}/api/v1/workspaces`

3. **Get Workspace by ID**
   - Method: GET
   - URL: `{{baseUrl}}/api/v1/workspaces/{{workspaceId}}`

4. **Get Workspaces by Owner**
   - Method: GET
   - URL: `{{baseUrl}}/api/v1/workspaces/owner/1`

5. **Update Workspace**
   - Method: PUT
   - URL: `{{baseUrl}}/api/v1/workspaces/{{workspaceId}}?userId=1`
   - Body: Raw JSON
   ```json
   {
     "name": "Updated Workspace Name",
     "description": "Updated description"
   }
   ```

6. **Delete Workspace**
   - Method: DELETE
   - URL: `{{baseUrl}}/api/v1/workspaces/{{workspaceId}}?userId=1`

**Environment Variables:**
```
baseUrl: http://localhost:8080
workspaceId: 1
ownerId: 1
```

---

## ✅ Test Checklist

### Basic CRUD:
- [ ] Tạo workspace thành công
- [ ] Lấy tất cả workspaces
- [ ] Lấy workspace theo ID
- [ ] Lấy workspaces theo owner
- [ ] Cập nhật workspace
- [ ] Xóa workspace

### Validation Tests:
- [ ] Tạo workspace với name rỗng → 400 Bad Request
- [ ] Tạo workspace với name quá ngắn (< 3 ký tự) → 400 Bad Request
- [ ] Tạo workspace với name quá dài (> 100 ký tự) → 400 Bad Request
- [ ] Lấy workspace với ID không tồn tại → 404 Not Found

### Integration Tests:
- [ ] Tạo workspace → Tạo mindmap trong workspace → Verify mindmap có workspaceId đúng
- [ ] Lấy tất cả mindmaps trong workspace
- [ ] Xóa workspace → Verify mindmaps cũng bị xóa (nếu có cascade delete)

---

## 🔗 Liên kết với Mindmap

### Lấy tất cả Mindmaps trong một Workspace:
```http
GET http://localhost:8080/api/mindmap/workspace/1
```

### Tạo Mindmap trong Workspace:
```http
POST http://localhost:8080/api/mindmap?ownerId=1
Content-Type: application/json

{
  "title": "Project Plan",
  "description": "Planning mindmap",
  "workspaceId": 1,
  "isPublic": false
}
```

---

## 🎯 Use Cases

### 1. Tổ chức theo Team
```
Workspace: "Engineering Team"
├── Mindmap: "Sprint Planning"
├── Mindmap: "Architecture Design"
└── Mindmap: "Bug Tracking"
```

### 2. Tổ chức theo Project
```
Workspace: "Mobile App Project"
├── Mindmap: "Feature Requirements"
├── Mindmap: "UI/UX Design"
└── Mindmap: "Testing Checklist"
```

### 3. Personal Organization
```
Workspace: "Personal"
├── Mindmap: "Career Goals"
├── Mindmap: "Learning Path"
└── Mindmap: "Side Projects"
```

---

## 🚀 Quick Start Script

Tạo workspace và mindmaps nhanh:

```bash
# 1. Tạo workspace
curl -X POST http://localhost:8080/api/v1/workspaces?ownerId=1 \
  -H "Content-Type: application/json" \
  -d '{"name":"My Team","description":"Team workspace"}'

# 2. Tạo mindmap trong workspace
curl -X POST http://localhost:8080/api/mindmap?ownerId=1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Project Plan","workspaceId":1}'

# 3. Lấy tất cả mindmaps trong workspace
curl http://localhost:8080/api/mindmap/workspace/1
```

---

## 📌 Notes

- **Owner ID**: Mặc định là 1 nếu không truyền vào (để test dễ dàng)
- **Security**: Hiện tại endpoints đang public để test, production cần thêm authentication
- **Cascade Delete**: Cần implement logic để xóa tất cả mindmaps khi xóa workspace
- **Member Management**: Tương lai có thể thêm API để quản lý members trong workspace

---

## 🎉 Done!

Workspace CRUD đã hoàn thiện với:
✅ Create, Read, Update, Delete operations
✅ Query by owner
✅ Integration với Mindmap
✅ Swagger documentation
✅ Validation đầy đủ

Bắt đầu test ngay tại: **http://localhost:8080/swagger-ui.html**

