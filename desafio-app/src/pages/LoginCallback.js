import React from 'react';
import { useAuth } from 'react-oidc-context';
import { Navigate } from 'react-router-dom';

const LoginCallback = () => {
  const auth = useAuth();
  console.log(auth);

  if (auth.isLoading) {
    return <div>Loading...</div>
  }

  if(auth.isAuthenticated) {
    return <Navigate to={'/upload'} />
  }

  return <div>Something happened</div>
}

export default LoginCallback;
