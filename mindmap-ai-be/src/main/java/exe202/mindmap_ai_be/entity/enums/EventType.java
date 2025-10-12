package exe202.mindmap_ai_be.entity.enums;

public enum EventType {
    // Node events
    ADD_NODE,
    UPDATE_NODE,
    DELETE_NODE,
    MOVE_NODE,
    UPDATE_NODE_COLOR,
    UPDATE_NODE_SHAPE,
    UPDATE_NODE_CONTENT,

    // Edge events
    ADD_EDGE,
    UPDATE_EDGE,
    UPDATE_EDGE_LABEL,
    DELETE_EDGE,

    // Sync events
    SYNC_REQUEST,
    SYNC_SNAPSHOT,

    // Cursor/User presence events
    USER_CURSOR_MOVE,
    USER_JOIN,
    USER_LEAVE,

    // Error event
    ERROR
}
