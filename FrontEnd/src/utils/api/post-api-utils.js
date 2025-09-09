import request from '../request';

export const apiNewPost = async (projectId, formData) =>
  await request.post(`/projects/${projectId}/post`, formData);

export const apiNewReply = async (projectId, formData) =>
  await request.post(`/projects/${projectId}/reply`, formData);

export const apiGetPosts = async (projectId) => {
  const res = await request.get(`/projects/${projectId}/posts`);
  return res.data;
};
