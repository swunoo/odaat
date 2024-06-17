import { render } from '@testing-library/react';
import Navbar, { activeStyle } from '../components/Navbar';

describe('Navbar', () => {
  test('renders the logo and navigation links correctly', () => {
    const { getByAltText, getByText } = render(<Navbar active="tasks" />);

    expect(getByAltText('Logo')).toBeInTheDocument();
    expect(getByText('tasks')).toBeInTheDocument();
    expect(getByText('projects')).toBeInTheDocument();
  });

  test('applies the active class to the active link', () => {
    const { getByText } = render(<Navbar active="tasks" />);

    const activeLink = getByText('tasks');
    const inactiveLink = getByText('projects');

    expect(activeLink).toHaveClass(activeStyle);
    expect(inactiveLink).not.toHaveClass(activeStyle);
  });
});