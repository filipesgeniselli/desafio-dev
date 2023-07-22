import React from 'react';
import './Navbar.css'; // Create a separate CSS file for styling
import { withAuth } from 'react-oidc-context';

class Navbar extends React.Component {
  render () {
    const auth = this.props.auth;
  
    function handleLogin() {
      auth.signinRedirect();
    }

    function handleLogout() {
      auth.signoutRedirect();
      auth.removeUser();
    }
    
    function drawProtectedFeatures() {
      return(
        <React.Fragment>
          <li><a href="/upload">Upload</a></li>
          <li><a href="/dashboard">Dashboard</a></li>
          <li><a href="/stores">Lojas</a></li>
        </React.Fragment>
      );
    }

    return (
      <nav className="navbar">
        <div className="navbar-container">
          <ul className="navbar-left">
            <li><a href="/">Página inicial</a></li>
            {auth.isAuthenticated ? drawProtectedFeatures() : null}
          </ul>
          <ul className="navbar-right">
            <li>
              {auth.isAuthenticated 
              ? <button className='btn-link' onClick={handleLogout}>Logout({auth.user?.profile.preferred_username})</button> 
              : <button className='btn-link' onClick={handleLogin}>Login</button>}
            </li>
          </ul>
        </div>
      </nav>
    );
  }
}

export default withAuth(Navbar);