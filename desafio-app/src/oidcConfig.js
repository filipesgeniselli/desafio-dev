const oidcConfig = {
  redirect_uri: 'http://localhost:3000/callback',
  client_id: 'desafio-app',
  authority: 'http://localhost:8080/realms/desafio/',
  onSigninCallback: () => {
    window.history.replaceState({}, document.title, window.location.pathname);
  },
};

export default oidcConfig;