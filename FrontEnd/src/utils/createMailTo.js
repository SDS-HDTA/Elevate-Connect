export const createMailTo = (subject = '', body = '') => {
  let mailto = `mailto:admin@elevateprograms.org?`;

  if (subject) {
    mailto += `subject=${encodeURIComponent(subject)}&`;
  }
  if (body) {
    mailto += `body=${encodeURIComponent(body)}&`;
  }

  // Remove trailing & or ? if present
  mailto = mailto.replace(/[&?]$/, '');

  return mailto;
};
