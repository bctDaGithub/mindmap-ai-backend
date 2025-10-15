# Collaboration API Guide

## Tổng quan
Tài liệu này mô tả các API để owner của mindmap/workspace có thể mời người dùng khác cùng cộng tác (collaboration).

## Permissions

### Mindmap Permissions
- **OWNER**: Chủ sở hữu mindmap
- **READ**: Chỉ xem
- **WRITE**: Có thể chỉnh sửa

### Workspace Permissions
- **OWNER**: Chủ sở hữu workspace
- **EDIT**: Có thể chỉnh sửa
- **VIEW_ONLY**: Chỉ xem

---

## Mindmap Collaboration APIs

### 1. Mời người dùng vào Mindmap
**Endpoint:** `POST /api/mindmap/{mindmapId}/invite`

**Mô tả:** Owner mời người dùng khác để cùng cộng tác trên mindmap

**Parameters:**
- `mindmapId` (path): ID của mindmap
- `targetUserId` (query): ID người dùng được mời
- `permission` (query): Quyền (READ hoặc WRITE)
- `ownerId` (query): ID của owner

**Example:**
```bash
POST /api/mindmap/1/invite?targetUserId=5&permission=WRITE&ownerId=1
```

**Response:** `204 No Content`

---

### 2. Xem danh sách thành viên Mindmap
**Endpoint:** `GET /api/mindmap/{mindmapId}/members`

**Mô tả:** Lấy danh sách tất cả thành viên đang có quyền trên mindmap

**Parameters:**
- `mindmapId` (path): ID của mindmap

**Example:**
```bash
GET /api/mindmap/1/members
```

**Response:**
```json
[
  {
    "userId": 5,
    "mindmapId": 1,
    "permission": "WRITE"
  },
  {
    "userId": 3,
    "mindmapId": 1,
    "permission": "READ"
  }
]
```

---

### 3. Cập nhật quyền thành viên Mindmap
**Endpoint:** `PUT /api/mindmap/{mindmapId}/members/{userId}/permission`

**Mô tả:** Owner cập nhật quyền của một thành viên

**Parameters:**
- `mindmapId` (path): ID của mindmap
- `userId` (path): ID của thành viên
- `permission` (query): Quyền mới (READ hoặc WRITE)
- `ownerId` (query): ID của owner

**Example:**
```bash
PUT /api/mindmap/1/members/5/permission?permission=READ&ownerId=1
```

**Response:** `204 No Content`

---

### 4. Xóa thành viên khỏi Mindmap
**Endpoint:** `DELETE /api/mindmap/{mindmapId}/members/{userId}`

**Mô tả:** Owner xóa một thành viên ra khỏi mindmap

**Parameters:**
- `mindmapId` (path): ID của mindmap
- `userId` (path): ID của thành viên cần xóa
- `ownerId` (query): ID của owner

**Example:**
```bash
DELETE /api/mindmap/1/members/5?ownerId=1
```

**Response:** `204 No Content`

---

## Workspace Collaboration APIs

### 1. Mời người dùng vào Workspace
**Endpoint:** `POST /api/v1/workspaces/{workspaceId}/invite`

**Mô tả:** Owner mời người dùng khác để cùng cộng tác trong workspace

**Parameters:**
- `workspaceId` (path): ID của workspace
- `targetUserId` (query): ID người dùng được mời
- `permission` (query): Quyền (EDIT hoặc VIEW_ONLY)
- `ownerId` (query): ID của owner

**Example:**
```bash
POST /api/v1/workspaces/1/invite?targetUserId=5&permission=EDIT&ownerId=1
```

**Response:** `204 No Content`

---

### 2. Xem danh sách thành viên Workspace
**Endpoint:** `GET /api/v1/workspaces/{workspaceId}/members`

**Mô tả:** Lấy danh sách tất cả thành viên trong workspace

**Parameters:**
- `workspaceId` (path): ID của workspace

**Example:**
```bash
GET /api/v1/workspaces/1/members
```

**Response:**
```json
[
  {
    "workspaceId": 1,
    "userId": 5,
    "workspacePermission": "EDIT"
  },
  {
    "workspaceId": 1,
    "userId": 3,
    "workspacePermission": "VIEW_ONLY"
  }
]
```

---

### 3. Cập nhật quyền thành viên Workspace
**Endpoint:** `PUT /api/v1/workspaces/{workspaceId}/members/{userId}/permission`

**Mô tả:** Owner cập nhật quyền của một thành viên trong workspace

**Parameters:**
- `workspaceId` (path): ID của workspace
- `userId` (path): ID của thành viên
- `permission` (query): Quyền mới (EDIT hoặc VIEW_ONLY)
- `ownerId` (query): ID của owner

**Example:**
```bash
PUT /api/v1/workspaces/1/members/5/permission?permission=VIEW_ONLY&ownerId=1
```

**Response:** `204 No Content`

---

### 4. Xóa thành viên khỏi Workspace
**Endpoint:** `DELETE /api/v1/workspaces/{workspaceId}/members/{userId}`

**Mô tả:** Owner xóa một thành viên ra khỏi workspace

**Parameters:**
- `workspaceId` (path): ID của workspace
- `userId` (path): ID của thành viên cần xóa
- `ownerId` (query): ID của owner

**Example:**
```bash
DELETE /api/v1/workspaces/1/members/5?ownerId=1
```

**Response:** `204 No Content`

---

## Validation Rules

### Mindmap Collaboration
1. Chỉ owner của mindmap mới có thể mời/xóa/cập nhật quyền thành viên
2. Không thể xóa owner khỏi mindmap
3. Người dùng đã có quyền không thể được mời lại (sẽ báo lỗi)
4. Permission phải là: READ hoặc WRITE

### Workspace Collaboration
1. Chỉ owner của workspace mới có thể mời/xóa/cập nhật quyền thành viên
2. Không thể xóa owner khỏi workspace
3. Người dùng đã là thành viên không thể được mời lại (sẽ báo lỗi)
4. Permission phải là: EDIT hoặc VIEW_ONLY (hoặc VIEW)

---

## Error Responses

### 400 Bad Request
```json
{
  "error": "User already has permission to this mindmap"
}
```

### 403 Forbidden
```json
{
  "error": "Only owner can invite users to mindmap"
}
```

### 404 Not Found
```json
{
  "error": "Mindmap not found"
}
```

---

## Testing với Swagger UI

Bạn có thể test các API này tại: `http://localhost:8080/swagger-ui.html`

1. Tìm section **Mindmap Real-time Collaboration** hoặc **Workspace**
2. Mở các endpoint collaboration
3. Click "Try it out"
4. Điền các parameters cần thiết
5. Click "Execute"

---

## Database Schema

### user_mindmap_permissions
```sql
CREATE TABLE user_mindmap_permissions (
    user_id BIGINT NOT NULL,
    mindmap_id BIGINT NOT NULL,
    permission VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, mindmap_id)
);
```

### workspace_members
```sql
CREATE TABLE workspace_members (
    workspace_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    workspace_permission VARCHAR(50) NOT NULL,
    PRIMARY KEY (workspace_id, user_id)
);
```

---

## Workflow Example

### Scenario: Owner mời 2 người vào mindmap để cộng tác

1. **Owner tạo mindmap:**
```bash
POST /api/mindmap
{
  "title": "Project Planning",
  "description": "Team collaboration",
  "workspaceId": 1,
  "isPublic": false
}
# Response: mindmapId = 10, ownerId = 1
```

2. **Owner mời user 5 với quyền WRITE:**
```bash
POST /api/mindmap/10/invite?targetUserId=5&permission=WRITE&ownerId=1
```

3. **Owner mời user 7 với quyền READ:**
```bash
POST /api/mindmap/10/invite?targetUserId=7&permission=READ&ownerId=1
```

4. **Xem danh sách thành viên:**
```bash
GET /api/mindmap/10/members
# Response: [
#   { userId: 5, permission: "WRITE" },
#   { userId: 7, permission: "READ" }
# ]
```

5. **Cập nhật quyền user 7 lên WRITE:**
```bash
PUT /api/mindmap/10/members/7/permission?permission=WRITE&ownerId=1
```

6. **Xóa user 5 ra khỏi mindmap:**
```bash
DELETE /api/mindmap/10/members/5?ownerId=1
```

---

## Notes

- Tất cả API đều yêu cầu authentication (trừ khi permit all trong SecurityConfig)
- Owner ID hiện tại đang dùng request param, trong production nên lấy từ JWT token
- Các thao tác collaboration sẽ được log và audit trail
- WebSocket sẽ tự động notify các thành viên khi có thay đổi về permissions

