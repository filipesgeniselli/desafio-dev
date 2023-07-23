const oidcConfig = {
  redirect_uri: process.env.REACT_APP_OIDC_REDIRECT_URI,
  client_id: 'desafio-app',
  authority: process.env.REACT_APP_OIDC_AUTHORITY,
  onSigninCallback: () => {
    window.history.replaceState({}, document.title, window.location.pathname);
  },
};

export default oidcConfig;