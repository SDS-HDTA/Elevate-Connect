export const disablePastDates = (time) => {
  const today = new Date();
  today.setHours(0, 0, 0, 0); // ignore time
  return time.getTime() < today.getTime();
};
