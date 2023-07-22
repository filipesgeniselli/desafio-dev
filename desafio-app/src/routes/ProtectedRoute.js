import React from 'react';
import { useAuth, withAuth } from 'react-oidc-context';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({redirectPath = '/', children}) => {
  const auth = useAuth();
  console.log(auth);

  if (auth.isLoading) {
    return <div>Loading...</div>
  }

  if(!auth.isAuthenticated) {
    return <Navigate to={redirectPath} />
  }

  return children;
}

export default withAuth(ProtectedRoute);
