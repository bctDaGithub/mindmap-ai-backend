# 🚀 Hướng dẫn Test Mindmap AI Backend

## 📋 Mục lục
1. [Chuẩn bị Database](#1-chuẩn-bị-database)
2. [Chạy ứng dụng](#2-chạy-ứng-dụng)
3. [Test REST APIs](#3-test-rest-apis)
4. [Test WebSocket](#4-test-websocket)

---

## 1. Chuẩn bị Database

### Tạo database PostgreSQL:
```sql
CREATE DATABASE mindmap_ai;
```

### Chạy schema script (đã tạo sẵn trong resources/schema.sql):
```sql
-- Hoặc copy nội dung từ src/main/resources/schema.sql và execute
```

---

## 2. Chạy ứng dụng

```bash
# Build project
mvnw clean install

# Chạy ứng dụng
mvnw spring-boot:run
```

Ứng dụng sẽ chạy tại: **http://localhost:8080**

### Truy cập Swagger UI:
**http://localhost:8080/swagger-ui.html**

---

## 3. Test REST APIs

### A. MINDMAP CRUD

#### 1️⃣ Tạo Mindmap mới
```http
POST http://localhost:8080/api/mindmap?ownerId=1
Content-Type: application/json

{
  "title": "My First Mindmap",
  "description": "This is a test mindmap",
  "workspaceId": null,
  "isPublic": false
}
```

**Response:**
```json
{
  "mindMapId": 1,
  "title": "My First Mindmap",
  "description": "This is a test mindmap",
  "workspaceId": null,
  "ownerId": 1,
  "isPublic": false,
  "createdAt": "2025-10-08T10:00:00.000+00:00",
  "updatedAt": "2025-10-08T10:00:00.000+00:00"
}
```

#### 2️⃣ Lấy tất cả Mindmaps
```http
GET http://localhost:8080/api/mindmap
```

#### 3️⃣ Lấy Mindmap theo ID
```http
GET http://localhost:8080/api/mindmap/1
```

#### 4️⃣ Lấy Mindmaps của user
```http
GET http://localhost:8080/api/mindmap/owner/1
```

#### 5️⃣ Cập nhật Mindmap
```http
PUT http://localhost:8080/api/mindmap/1?userId=1
Content-Type: application/json

{
  "title": "Updated Mindmap Title",
  "description": "Updated description",
  "isPublic": true
}
```

#### 6️⃣ Xóa Mindmap
```http
DELETE http://localhost:8080/api/mindmap/1?userId=1
```

---

### B. NODE CRUD

#### 1️⃣ Tạo Node mới
```http
POST http://localhost:8080/api/mindmap/1/nodes
Content-Type: application/json

{
  "content": "Central Topic",
  "positionX": 400,
  "positionY": 300,
  "color": "#FF6B6B",
  "shape": "CIRCLE",
  "parentNodeId": null
}
```

**Response:**
```json
{
  "nodeId": 1,
  "mindMapId": 1,
  "parentNodeId": null,
  "content": "Central Topic",
  "positionX": 400.0,
  "positionY": 300.0,
  "color": "#FF6B6B",
  "shape": "CIRCLE",
  "createAt": "2025-10-08T10:00:00Z",
  "version": 0
}
```

#### 2️⃣ Lấy tất cả Nodes của Mindmap
```http
GET http://localhost:8080/api/mindmap/1/nodes
```

#### 3️⃣ Lấy Node theo ID
```http
GET http://localhost:8080/api/mindmap/nodes/1
```

#### 4️⃣ Di chuyển Node
```http
PATCH http://localhost:8080/api/mindmap/nodes/1/move?positionX=450&positionY=350
```

#### 5️⃣ Đổi màu Node
```http
PATCH http://localhost:8080/api/mindmap/nodes/1/color?color=#4ECDC4
```

#### 6️⃣ Đổi hình dạng Node
```http
PATCH http://localhost:8080/api/mindmap/nodes/1/shape?shape=RECTANGLE
```

**Shapes available:** RECTANGLE, CIRCLE, ELLIPSE, DIAMOND, ROUNDED_RECTANGLE

#### 7️⃣ Cập nhật nội dung Node
```http
PATCH http://localhost:8080/api/mindmap/nodes/1/content?content=Updated Content
```

#### 8️⃣ Cập nhật toàn bộ Node
```http
PUT http://localhost:8080/api/mindmap/nodes/1
Content-Type: application/json

{
  "nodeId": 1,
  "mindMapId": 1,
  "content": "Updated Central Topic",
  "positionX": 500,
  "positionY": 400,
  "color": "#45B7D1",
  "shape": "ELLIPSE",
  "parentNodeId": null
}
```

#### 9️⃣ Xóa Node
```http
DELETE http://localhost:8080/api/mindmap/nodes/1
```

---

### C. EDGE CRUD

#### 1️⃣ Tạo Edge mới (kết nối 2 nodes)
```http
POST http://localhost:8080/api/mindmap/1/edges
Content-Type: application/json

{
  "fromNodeId": 1,
  "toNodeId": 2,
  "label": "connects to"
}
```

**Response:**
```json
{
  "id": 1,
  "mindmapId": 1,
  "fromNodeId": 1,
  "toNodeId": 2,
  "label": "connects to"
}
```

#### 2️⃣ Lấy tất cả Edges của Mindmap
```http
GET http://localhost:8080/api/mindmap/1/edges
```

#### 3️⃣ Lấy Edge theo ID
```http
GET http://localhost:8080/api/mindmap/edges/1
```

#### 4️⃣ Cập nhật label của Edge
```http
PATCH http://localhost:8080/api/mindmap/edges/1/label?label=new relationship
```

#### 5️⃣ Cập nhật toàn bộ Edge
```http
PUT http://localhost:8080/api/mindmap/edges/1
Content-Type: application/json

{
  "id": 1,
  "mindmapId": 1,
  "fromNodeId": 1,
  "toNodeId": 3,
  "label": "updated connection"
}
```

#### 6️⃣ Xóa Edge
```http
DELETE http://localhost:8080/api/mindmap/edges/1
```

---

## 4. Test WebSocket

### Kết nối WebSocket:
```
ws://localhost:8080/ws/mindmap?mindmapId=1&userId=123
```

### Tool để test WebSocket:
- **Postman** (có hỗ trợ WebSocket)
- **WebSocket King Client** (Chrome Extension)
- **wscat** (CLI tool)

### A. Test với wscat (CLI)

#### Cài đặt wscat:
```bash
npm install -g wscat
```

#### Kết nối:
```bash
wscat -c "ws://localhost:8080/ws/mindmap?mindmapId=1&userId=123"
```

### B. Các Event Messages để Test

#### 1. Khi kết nối xong, bạn sẽ nhận được:
```json
{
  "eventType": "USER_JOIN",
  "mindMapId": 1,
  "userId": 123,
  "entity": "user",
  "payload": {
    "userId": 123,
    "activeUsers": 1
  },
  "timestamp": "2025-10-08T10:00:00Z"
}
```

```json
{
  "eventType": "SYNC_SNAPSHOT",
  "mindMapId": 1,
  "userId": null,
  "entity": "snapshot",
  "payload": {
    "nodes": [...],
    "edges": [...]
  },
  "timestamp": "2025-10-08T10:00:00Z"
}
```

#### 2. Thêm Node mới
```json
{
  "eventType": "ADD_NODE",
  "payload": {
    "content": "New Node via WebSocket",
    "positionX": 200,
    "positionY": 200,
    "color": "#FFEAA7",
    "shape": "RECTANGLE"
  }
}
```

#### 3. Di chuyển Node
```json
{
  "eventType": "MOVE_NODE",
  "payload": {
    "nodeId": 1,
    "positionX": 300,
    "positionY": 400
  }
}
```

#### 4. Đổi màu Node
```json
{
  "eventType": "UPDATE_NODE_COLOR",
  "payload": {
    "nodeId": 1,
    "color": "#FF0000"
  }
}
```

#### 5. Đổi hình dạng Node
```json
{
  "eventType": "UPDATE_NODE_SHAPE",
  "payload": {
    "nodeId": 1,
    "shape": "CIRCLE"
  }
}
```

#### 6. Cập nhật nội dung Node
```json
{
  "eventType": "UPDATE_NODE_CONTENT",
  "payload": {
    "nodeId": 1,
    "content": "Updated content via WebSocket"
  }
}
```

#### 7. Xóa Node
```json
{
  "eventType": "DELETE_NODE",
  "payload": {
    "nodeId": 1
  }
}
```

#### 8. Thêm Edge
```json
{
  "eventType": "ADD_EDGE",
  "payload": {
    "fromNodeId": 1,
    "toNodeId": 2,
    "label": "connects"
  }
}
```

#### 9. Cập nhật Edge label
```json
{
  "eventType": "UPDATE_EDGE_LABEL",
  "payload": {
    "edgeId": 1,
    "label": "new label"
  }
}
```

#### 10. Xóa Edge
```json
{
  "eventType": "DELETE_EDGE",
  "payload": {
    "edgeId": 1
  }
}
```

#### 11. Di chuyển Cursor (real-time presence)
```json
{
  "eventType": "USER_CURSOR_MOVE",
  "payload": {
    "x": 500,
    "y": 600,
    "userName": "John Doe"
  }
}
```

#### 12. Yêu cầu đồng bộ lại
```json
{
  "eventType": "SYNC_REQUEST",
  "payload": {}
}
```

---

## 5. Test Flow Đầy Đủ

### Scenario: Tạo mindmap hoàn chỉnh từ đầu

#### Bước 1: Tạo Mindmap
```bash
POST /api/mindmap?ownerId=1
Body: {"title": "Project Plan", "description": "Planning for Q4"}
# Nhận mindMapId = 1
```

#### Bước 2: Tạo Node gốc
```bash
POST /api/mindmap/1/nodes
Body: {"content": "Project Root", "positionX": 400, "positionY": 300, "color": "#FF6B6B", "shape": "CIRCLE"}
# Nhận nodeId = 1
```

#### Bước 3: Tạo các Node con
```bash
POST /api/mindmap/1/nodes
Body: {"content": "Phase 1", "positionX": 200, "positionY": 200, "color": "#4ECDC4", "shape": "RECTANGLE", "parentNodeId": 1}
# Nhận nodeId = 2

POST /api/mindmap/1/nodes
Body: {"content": "Phase 2", "positionX": 600, "positionY": 200, "color": "#45B7D1", "shape": "RECTANGLE", "parentNodeId": 1}
# Nhận nodeId = 3
```

#### Bước 4: Tạo Edges kết nối
```bash
POST /api/mindmap/1/edges
Body: {"fromNodeId": 1, "toNodeId": 2, "label": "starts with"}

POST /api/mindmap/1/edges
Body: {"fromNodeId": 1, "toNodeId": 3, "label": "then"}
```

#### Bước 5: Kết nối WebSocket và test real-time
```bash
wscat -c "ws://localhost:8080/ws/mindmap?mindmapId=1&userId=123"
```

#### Bước 6: Mở tab/window khác, kết nối WebSocket thứ 2
```bash
wscat -c "ws://localhost:8080/ws/mindmap?mindmapId=1&userId=456"
```

#### Bước 7: Từ connection 1, di chuyển node
```json
{"eventType": "MOVE_NODE", "payload": {"nodeId": 2, "positionX": 250, "positionY": 250}}
```

➡️ Connection 2 sẽ nhận được event này real-time!

---

## 6. Test với Postman

### Import Collection

Tạo Postman Collection với các requests sau:

**Folder 1: Mindmap CRUD**
- Create Mindmap (POST)
- Get All Mindmaps (GET)
- Get Mindmap by ID (GET)
- Update Mindmap (PUT)
- Delete Mindmap (DELETE)

**Folder 2: Node Operations**
- Create Node (POST)
- Get All Nodes (GET)
- Move Node (PATCH)
- Update Color (PATCH)
- Update Shape (PATCH)
- Update Content (PATCH)
- Delete Node (DELETE)

**Folder 3: Edge Operations**
- Create Edge (POST)
- Get All Edges (GET)
- Update Label (PATCH)
- Delete Edge (DELETE)

**Folder 4: WebSocket**
- New Request → WebSocket Request
- URL: `ws://localhost:8080/ws/mindmap?mindmapId=1&userId=123`

---

## 7. Troubleshooting

### ❌ Lỗi kết nối database
```
Kiểm tra:
- PostgreSQL đã chạy chưa?
- Database "mindmap_ai" đã tạo chưa?
- application.properties có đúng config không?
```

### ❌ Lỗi 401 Unauthorized
```
Kiểm tra SecurityConfig đã permitAll cho:
- /api/mindmap/**
- /ws/**
```

### ❌ WebSocket không kết nối
```
Kiểm tra:
- URL có đúng format: ws://localhost:8080/ws/mindmap?mindmapId=1&userId=123
- SecurityConfig có allow WebSocket endpoint không
- CORS configuration
```

### ❌ Không nhận được real-time updates
```
Kiểm tra:
- Có đang kết nối đúng mindmapId không?
- Message format JSON có đúng không?
- Logs trong console có báo lỗi gì không?
```

---

## 8. Sample Test Data

Để test nhanh, bạn có thể dùng script này:

```sql
-- Insert sample mindmap
INSERT INTO mindmaps (mind_map_id, title, description, owner_id, is_public, created_at, updated_at)
VALUES (1, 'Test Mindmap', 'For testing purposes', 1, false, NOW(), NOW());

-- Insert sample nodes
INSERT INTO nodes (node_id, mind_map_id, content, position_x, position_y, color, shape, create_at)
VALUES 
(1, 1, 'Root', 400, 300, '#FF6B6B', 'CIRCLE', NOW()),
(2, 1, 'Child 1', 200, 200, '#4ECDC4', 'RECTANGLE', NOW()),
(3, 1, 'Child 2', 600, 200, '#45B7D1', 'RECTANGLE', NOW());

-- Insert sample edges
INSERT INTO edges (id, mindmap_id, from_node_id, to_node_id, label)
VALUES 
(1, 1, 1, 2, 'connects to'),
(2, 1, 1, 3, 'connects to');
```

---

## 🎉 Kết luận

Bây giờ bạn đã có:
✅ Đầy đủ CRUD cho Mindmap, Node, Edge
✅ Real-time collaboration với WebSocket
✅ Swagger UI để test APIs
✅ Sample data và requests để bắt đầu test

**Next steps:**
- Test từng endpoint trong Swagger UI
- Test WebSocket với 2 connections cùng lúc
- Thử tạo một mindmap hoàn chỉnh
- Test real-time updates giữa nhiều users

Happy Testing! 🚀

