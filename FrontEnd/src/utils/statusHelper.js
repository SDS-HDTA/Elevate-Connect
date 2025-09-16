// Get tag type for status
export const getStatusType = (status) => {
  const types = {
    0: 'danger', // Empathise
    1: 'danger', // Discover
    2: 'warning', // Define
    3: 'warning', // Ideate
    4: 'primary', // Prototype
    5: 'success', // Feedback
  };
  return types[status] || 'primary';
};

export const getPercentageStatusType = (status) => {
  const types = {
    0: 'exception', // Empathise
    1: 'exception', // Discover
    2: 'warning', // Define
    3: 'warning', // Ideate
    4: null, // Prototype
    5: 'success', // Feedback
  };
  return types[status] || null;
};

// Get status display text
export const getStatusText = (status) => {
  const texts = {
    0: 'Empathise',
    1: 'Discover',
    2: 'Define',
    3: 'Ideate',
    4: 'Prototype',
    5: 'Feedback',
  };
  return texts[status] || 'Unknown';
};
