# Mindmap AI Backend - Real-time Collaboration

## 🚀 Tính năng Real-time Collaboration

Hệ thống hỗ trợ real-time collaboration cho mindmap sử dụng WebSocket với Spring WebFlux.

## 📡 WebSocket Endpoint

```
ws://localhost:8080/ws/mindmap?mindmapId={mindmapId}&userId={userId}
```

### Parameters:
- `mindmapId`: ID của mindmap cần kết nối
- `userId`: ID của user đang kết nối

## 📨 Message Format

Tất cả messages đều theo format JSON:

```json
{
  "eventType": "ADD_NODE",
  "mindMapId": 1,
  "userId": 123,
  "entity": "node",
  "payload": { ... },
  "timestamp": "2025-10-08T10:30:00Z"
}
```

## 🎯 Event Types

### Node Events

#### 1. ADD_NODE - Thêm node mới
```json
{
  "eventType": "ADD_NODE",
  "payload": {
    "content": "New Node",
    "positionX": 100.0,
    "positionY": 200.0,
    "color": "#FF5733",
    "shape": "RECTANGLE",
    "parentNodeId": null
  }
}
```

#### 2. UPDATE_NODE - Cập nhật toàn bộ node
```json
{
  "eventType": "UPDATE_NODE",
  "payload": {
    "nodeId": 1,
    "content": "Updated Node",
    "positionX": 150.0,
    "positionY": 250.0,
    "color": "#33FF57",
    "shape": "CIRCLE"
  }
}
```

#### 3. MOVE_NODE - Di chuyển node
```json
{
  "eventType": "MOVE_NODE",
  "payload": {
    "nodeId": 1,
    "positionX": 300.0,
    "positionY": 400.0
  }
}
```

#### 4. UPDATE_NODE_COLOR - Đổi màu node
```json
{
  "eventType": "UPDATE_NODE_COLOR",
  "payload": {
    "nodeId": 1,
    "color": "#FF0000"
  }
}
```

#### 5. UPDATE_NODE_SHAPE - Đổi hình dạng node
```json
{
  "eventType": "UPDATE_NODE_SHAPE",
  "payload": {
    "nodeId": 1,
    "shape": "ELLIPSE"
  }
}
```

#### 6. UPDATE_NODE_CONTENT - Đổi nội dung node
```json
{
  "eventType": "UPDATE_NODE_CONTENT",
  "payload": {
    "nodeId": 1,
    "content": "New content text"
  }
}
```

#### 7. DELETE_NODE - Xóa node
```json
{
  "eventType": "DELETE_NODE",
  "payload": {
    "nodeId": 1
  }
}
```

### Edge Events

#### 8. ADD_EDGE - Thêm edge mới
```json
{
  "eventType": "ADD_EDGE",
  "payload": {
    "fromNodeId": 1,
    "toNodeId": 2,
    "label": "connects to"
  }
}
```

#### 9. UPDATE_EDGE - Cập nhật edge
```json
{
  "eventType": "UPDATE_EDGE",
  "payload": {
    "id": 1,
    "fromNodeId": 1,
    "toNodeId": 3,
    "label": "new connection"
  }
}
```

#### 10. UPDATE_EDGE_LABEL - Cập nhật label của edge
```json
{
  "eventType": "UPDATE_EDGE_LABEL",
  "payload": {
    "edgeId": 1,
    "label": "updated label"
  }
}
```

#### 11. DELETE_EDGE - Xóa edge
```json
{
  "eventType": "DELETE_EDGE",
  "payload": {
    "edgeId": 1
  }
}
```

### User Presence Events

#### 12. USER_CURSOR_MOVE - Di chuyển cursor
```json
{
  "eventType": "USER_CURSOR_MOVE",
  "payload": {
    "x": 500.0,
    "y": 600.0,
    "userName": "John Doe"
  }
}
```

### Sync Events

#### 13. SYNC_REQUEST - Yêu cầu đồng bộ
```json
{
  "eventType": "SYNC_REQUEST",
  "payload": {}
}
```

Server sẽ trả về:

#### 14. SYNC_SNAPSHOT - Snapshot đầy đủ
```json
{
  "eventType": "SYNC_SNAPSHOT",
  "payload": {
    "nodes": [...],
    "edges": [...]
  }
}
```

## 🔧 REST API Endpoints

### Swagger UI
Truy cập: `http://localhost:8080/swagger-ui.html`

### Node APIs

- `POST /api/mindmap/{mindmapId}/nodes` - Tạo node mới
- `GET /api/mindmap/{mindmapId}/nodes` - Lấy tất cả nodes
- `GET /api/mindmap/nodes/{nodeId}` - Lấy node theo ID
- `PUT /api/mindmap/nodes/{nodeId}` - Cập nhật node
- `PATCH /api/mindmap/nodes/{nodeId}/move` - Di chuyển node
- `PATCH /api/mindmap/nodes/{nodeId}/color` - Đổi màu node
- `PATCH /api/mindmap/nodes/{nodeId}/shape` - Đổi hình dạng node
- `PATCH /api/mindmap/nodes/{nodeId}/content` - Đổi nội dung node
- `DELETE /api/mindmap/nodes/{nodeId}` - Xóa node

### Edge APIs

- `POST /api/mindmap/{mindmapId}/edges` - Tạo edge mới
- `GET /api/mindmap/{mindmapId}/edges` - Lấy tất cả edges
- `GET /api/mindmap/edges/{edgeId}` - Lấy edge theo ID
- `PUT /api/mindmap/edges/{edgeId}` - Cập nhật edge
- `PATCH /api/mindmap/edges/{edgeId}/label` - Cập nhật label
- `DELETE /api/mindmap/edges/{edgeId}` - Xóa edge

## 🎨 Shape Types

Các shape được hỗ trợ (enum):
- `RECTANGLE`
- `CIRCLE`
- `ELLIPSE`
- `DIAMOND`
- `ROUNDED_RECTANGLE`

## 💡 Ví dụ sử dụng với JavaScript

```javascript
// Kết nối WebSocket
const ws = new WebSocket('ws://localhost:8080/ws/mindmap?mindmapId=1&userId=123');

ws.onopen = () => {
  console.log('Connected to mindmap');
  
  // Thêm node mới
  ws.send(JSON.stringify({
    eventType: 'ADD_NODE',
    payload: {
      content: 'My Node',
      positionX: 100,
      positionY: 200,
      color: '#FF5733',
      shape: 'RECTANGLE'
    }
  }));
};

ws.onmessage = (event) => {
  const message = JSON.parse(event.data);
  console.log('Received:', message);
  
  switch(message.eventType) {
    case 'SYNC_SNAPSHOT':
      // Nhận snapshot ban đầu
      console.log('Nodes:', message.payload.nodes);
      console.log('Edges:', message.payload.edges);
      break;
    case 'USER_JOIN':
      console.log('User joined:', message.payload.userId);
      break;
    case 'MOVE_NODE':
      // Cập nhật vị trí node trên UI
      updateNodePosition(message.payload.node);
      break;
    // ... xử lý các event khác
  }
};

// Di chuyển node
function moveNode(nodeId, x, y) {
  ws.send(JSON.stringify({
    eventType: 'MOVE_NODE',
    payload: { nodeId, positionX: x, positionY: y }
  }));
}

// Đổi màu node
function changeNodeColor(nodeId, color) {
  ws.send(JSON.stringify({
    eventType: 'UPDATE_NODE_COLOR',
    payload: { nodeId, color }
  }));
}
```

## 🔐 Security

WebSocket endpoint yêu cầu authentication. Đảm bảo pass JWT token khi kết nối hoặc cấu hình trong SecurityConfig.

## 🚦 Chạy ứng dụng

```bash
# Build project
mvnw clean install

# Run application
mvnw spring-boot:run
```

Ứng dụng sẽ chạy tại: `http://localhost:8080`

## 📚 Documentation

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 🎯 Testing với Postman/Thunder Client

1. Tạo WebSocket request tới: `ws://localhost:8080/ws/mindmap?mindmapId=1&userId=123`
2. Gửi message theo format JSON ở trên
3. Quan sát response real-time

## ⚡ Performance

- Sử dụng Spring WebFlux với Reactor để xử lý non-blocking
- Mỗi mindmap có một broadcast channel riêng
- Hỗ trợ nhiều user cùng lúc trên cùng mindmap
- Auto cleanup khi không còn user nào

## 🐛 Troubleshooting

### WebSocket không kết nối được
- Kiểm tra SecurityConfig có cho phép WebSocket endpoint
- Kiểm tra CORS configuration
- Đảm bảo mindmapId và userId hợp lệ

### Không nhận được events
- Kiểm tra format JSON đúng
- Kiểm tra eventType có trong enum EventType
- Xem logs để debug

## 📝 Notes

- Tất cả events đều được broadcast tới tất cả clients trong cùng mindmap room
- Cursor move events không được persist vào database (chỉ broadcast)
- Initial snapshot được gửi tự động khi client mới kết nối
- Active users được track và broadcast khi có user join/leave

