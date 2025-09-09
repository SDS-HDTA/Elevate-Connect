export const getWebSocketURL = (projectId) => {
  return `ws://localhost:8080/projects/${projectId}/post`;
};

//Enum using Object.freeze()
export const MessageType = Object.freeze({
  NEW_REPLY: 'new_reply',
  NEW_POST: 'new_post',
  DELETE_REPLY: 'delete_reply',
  DELETE_POST: 'delete_post',
});
