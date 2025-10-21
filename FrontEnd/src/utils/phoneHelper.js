export const sanitizePhoneNumber = (event) => {
  // Allow only +, numbers and spaces
  const allowedChars = /^[+0-9\s]$/;
  if (!allowedChars.test(event.key)) {
    event.preventDefault();
  }
};
