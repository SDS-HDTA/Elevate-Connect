// Get tag type for status
export const getStageType = (stage) => {
  return stage === 5 ? 'success' : null;
};

// Get project stage display text
export const getProjectStageText = (stage) => {
  const texts = {
    0: 'Empathise',
    1: 'Discover',
    2: 'Define',
    3: 'Ideate',
    4: 'Prototype',
    5: 'Completed',
  };
  return texts[stage] || 'Unknown';
};
