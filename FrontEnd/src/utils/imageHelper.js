export const MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 10 MB

export const getLegibleFileSize = () =>
  Math.floor(MAX_FILE_SIZE_BYTES / 1024 / 1024);
