export const getProgressPercentage = (stage) => {
  const totalStages = 6; // 0-5
  if (stage < 0) return 0;
  if (stage >= totalStages) return 100;
  return Math.round(((stage + 1) / totalStages) * 100);
};
