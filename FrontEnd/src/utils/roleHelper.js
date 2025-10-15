export const roleMap = {
  0: 'Community Insight Partner',
  1: 'Country Collaboration Partner',
  2: 'Humanitarian Impact Partner',
  3: 'Elevate Facilitation Lead',
};

export const getUserRole = (role) => {
  return roleMap[role] || 'Unknown Role';
};

export const getUserRoleClass = (role) => {
  const roleMap = {
    0: 'danger', // 'Community Insight Partner'
    1: 'warning', // 'Country Collaboration Partner'
    2: 'primary', // 'Humanitarian Impact Partner'
    3: 'success', // 'Elevate Facilitation Lead'
  };
  return roleMap[role] || 'Unknown Role';
};

export const roleOrder = [3, 2, 1, 0];
