export const MIRO_CONFIG = {
  clientId: process.env.VUE_APP_MIRO_CLIENT_ID,
  clientSecret: process.env.VUE_APP_MIRO_CLIENT_SECRET,
  redirectUri: process.env.VUE_APP_MIRO_REDIRECT_URI,
  scopes: [
    'boards:read',
    'boards:write',
    'boards:write:team',
    'boards:read:team'
  ]
} 