// Get tag type for status
export const getStatusType = (status) => {
  return status === 5 ? 'success' : null;
};

export const getPercentageStatusType = (status) => {
  return status === 5 ? 'success' : null;
};

// Get status display text
export const getStatusText = (status) => {
  const texts = {
    0: 'Empathise',
    1: 'Discover',
    2: 'Define',
    3: 'Ideate',
    4: 'Prototype',
    5: 'Completed',
  };
  return texts[status] || 'Unknown';
};
