export const getProgressPercentage = (status) => {
  const totalStages = 6; // 0-5
  if (status < 0) return 0;
  if (status >= totalStages) return 100;
  return Math.round(((status + 1) / totalStages) * 100);
};
