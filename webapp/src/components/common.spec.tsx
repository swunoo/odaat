import { render, fireEvent } from '@testing-library/react';
import {
  NewProjButton,
  NewTaskButton,
  PlusButton,
  SvgChevronLeft,
  SvgChevronRight
} from './common';

describe('NewProjButton Component', () => {
  test('renders NewProjButton component', () => {
    const onClick = jest.fn();

    const { getByText } = render(<NewProjButton clickHandler={onClick} />);

    const button = getByText('New Project');
    expect(button).toBeInTheDocument();

    fireEvent.click(button);
    expect(onClick).toHaveBeenCalledTimes(1);
  });
});

describe('NewTaskButton Component', () => {
  test('renders NewTaskButton component', () => {
    const onClick = jest.fn();

    const { getByText } = render(<NewTaskButton clickHandler={onClick} />);

    const button = getByText('New Task');
    expect(button).toBeInTheDocument();

    fireEvent.click(button);
    expect(onClick).toHaveBeenCalledTimes(1);
  });
});

describe('NewButton Component', () => {
  test('renders NewButton component', () => {
    const onClick = jest.fn();

    const { getByText } = render(<PlusButton label="New Project" clickHandler={onClick} />);

    const button = getByText('New Project');
    expect(button).toBeInTheDocument();

    fireEvent.click(button);
    expect(onClick).toHaveBeenCalledTimes(1);
  });
});

describe('SvgChevronLeft Component', () => {
  test('renders SvgChevronLeft component', () => {
    const onClick = jest.fn();

    const { container } = render(<SvgChevronLeft clickHandler={onClick} />);

    const svgButton = container.firstChild;
    expect(svgButton).toBeInTheDocument();

    fireEvent.click(svgButton!);
    expect(onClick).toHaveBeenCalledTimes(1);
  });
});

describe('SvgChevronRight Component', () => {
  test('renders SvgChevronRight component', () => {
    const onClick = jest.fn();

    const { container } = render(<SvgChevronRight clickHandler={onClick} />);

    const svgButton = container.firstChild;
    expect(svgButton).toBeInTheDocument();

    fireEvent.click(svgButton!);
    expect(onClick).toHaveBeenCalledTimes(1);
  });
});