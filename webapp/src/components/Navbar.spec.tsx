import { render, screen } from '@testing-library/react';
import { activeStyle, Navbar } from './Navbar';
import { AuthContext, LoadingContext, SyncContext } from '../App';

describe('Navbar', () => {

  test('renders login button when not authenticated', () => {
    render(
      <AuthContext.Provider value={ { authenticated: false, setAuthenticated: ()=>{} } }>
        <LoadingContext.Provider value={ { loading: false, setLoading: ()=>{} } }>
          <SyncContext.Provider value={ { sync: false, setSync: ()=>{} } }>
            <Navbar active="tasks" />
          </SyncContext.Provider>
        </LoadingContext.Provider>
      </AuthContext.Provider>
    );
    expect(screen.getByText('Log In')).toBeInTheDocument();
  });

  test('renders logout button and navigation links when authenticated', () => {
    render(
      <AuthContext.Provider value={ { authenticated: true, setAuthenticated: ()=>{} } }>
        <LoadingContext.Provider value={ { loading: false, setLoading: ()=>{} } }>
          <SyncContext.Provider value={ { sync: false, setSync: ()=>{} } }>
            <Navbar active="tasks" />
          </SyncContext.Provider>
        </LoadingContext.Provider>
      </AuthContext.Provider>
    );
    expect(screen.getByText('Log Out')).toBeInTheDocument();
    expect(screen.getByText('tasks')).toBeInTheDocument();
    expect(screen.getByText('projects')).toBeInTheDocument();
  });

  test('navigation links have correct styles', () => {
    render(
      <AuthContext.Provider value={ { authenticated: true, setAuthenticated: ()=>{} } }>
        <LoadingContext.Provider value={ { loading: false, setLoading: ()=>{} } }>
          <SyncContext.Provider value={ { sync: false, setSync: ()=>{} } }>
            <Navbar active="tasks" />
          </SyncContext.Provider>
        </LoadingContext.Provider>
      </AuthContext.Provider>
    );
    expect(screen.getByText('tasks')).toHaveClass(activeStyle);
    expect(screen.getByText('projects')).not.toHaveClass(activeStyle);
  });
});
