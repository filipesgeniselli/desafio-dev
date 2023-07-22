import React from 'react';
import { withAuth } from 'react-oidc-context';
import { Navigate } from 'react-router-dom';

class LoginCallback extends React.Component {
  render() {
    const auth = this.props.auth;
  
    if (auth.isAuthenticated){
      return <Navigate to={'/upload'} />
    }

    return <div>Something happened</div>
  }
}

export default withAuth(LoginCallback);
