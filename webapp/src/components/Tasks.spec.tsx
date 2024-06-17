import { act, fireEvent, render, waitFor } from '@testing-library/react';
import { ProjectData } from '../conf';
import { MILLIS_A_DAY, formatTime } from '../utils';
import Tasks, { TaskContainer } from './Tasks';
import { VoidFunc } from './common';

// Mock data, components, and assets
const mockDate = new Date();
const mockProject: ProjectData = {
  id: 1,
  category: { id: 1, name: 'Category' },
  name: 'Test Project',
  description: 'string',
  status: 'STARTED',
  priority: 'LOW',
  startTime: mockDate,
  endTime: mockDate,
  dueTime: mockDate,
  estimatedHr: 1,
  dailyHr: 1,
  createdAt: mockDate,
  updatedAt: mockDate
};

// Mocking the calendar icon image
jest.mock('../assets/calendarIcon.png', () => 'calendar-icon.png');

// Mocking common components
jest.mock('./common', () => ({
  SvgChevronLeft: ({clickHandler}: { clickHandler: VoidFunc }) => <button onClick={clickHandler}>Left</button>,
  SvgChevronRight: ({clickHandler}: { clickHandler: VoidFunc }) => <button onClick={clickHandler}>Right</button>
}));

// Mocking the TaskWrapper component
jest.mock('./TaskWrapper', () => ({
  TaskWrapper: ({date, addProject, newProj}: any) => (
      <div> {date.toString()} <span>{newProj?.name}</span> <button onClick={addProject}>New Project</button></div>
  )
}));

// Mocking the Projects component
jest.mock('./Projects', () => ({
  NewProjectModal: ({ data, cancelHandler, projectSetter }: any) => (
    <div data-testid="new-project-modal">
      {data}
      <button onClick={() => cancelHandler()}>Cancel</button>
      <button onClick={() => projectSetter({ name: 'New Project' })}>New Project</button>
    </div>
  )
}));

describe('Tasks Component', () => {

  let rendered: any;

  // Render the Tasks component before each test
  beforeEach(() => {
    rendered = render(<Tasks />);
  });

  // Test to check if the Navbar renders correctly
  test('renders Navbar', () => {
    const { getByText } = rendered;
    expect(getByText('projects')).toBeInTheDocument();
    expect(getByText('tasks')).toBeInTheDocument();
  });

  // Test to check if NewProjectModal is displayed when setShowNewProj is called
  test('shows NewProjectModal when setShowNewProj is called', () => {
    const { getByText, getByTestId } = rendered;

    fireEvent.click(getByText('New Project'));
    expect(getByTestId('new-project-modal')).toBeInTheDocument();
  });

  // Test to check if NewProjectModal is hidden when cancelHandler is called
  test('hides NewProjectModal when cancelHandler is called', () => {
    const { getByText, getByTestId, queryByTestId } = rendered;

    fireEvent.click(getByText('New Project'));
    expect(getByTestId('new-project-modal')).toBeInTheDocument();

    fireEvent.click(getByText('Cancel'));
    expect(queryByTestId('new-project-modal')).not.toBeInTheDocument();
  });

  // Test to check if onProjectCreate updates newProjTitle and hides NewProjectModal
  test('onProjectCreate updates newProjTitle and hides NewProjectModal', () => {
    const { getAllByText, queryByTestId } = rendered;

    fireEvent.click(getAllByText('New Project')[0]);
    expect(queryByTestId('new-project-modal')).toBeInTheDocument();

    fireEvent.click(getAllByText('New Project')[0]);
    expect(queryByTestId('new-project-modal')).not.toBeInTheDocument();
  });
});

describe('TaskContainer', () => {
  let setShowNewProj: jest.Mock;
  let rendered: any;

  // Render the TaskContainer component before each test
  beforeEach(() => {
    setShowNewProj = jest.fn();
    rendered = render(<TaskContainer newProj={mockProject} setShowNewProj={setShowNewProj} />);
  });

  // Test to check if TaskContainer renders correctly with all elements
  test('renders correctly with all elements', () => {
    const { getByText, getByAltText } = rendered;

    expect(getByAltText('Change Date')).toBeInTheDocument();
    expect(getByText(formatTime(new Date(), 'full'))).toBeInTheDocument();
    expect(getByText('Test Project')).toBeInTheDocument();
  });

  // Test to check if date increments when the right chevron is clicked
  test('increments date when right chevron is clicked', async () => {
    const { getByText } = rendered;
    const rightChevron = getByText('Right');

    const initialDate = new Date();
    await act(async () => { fireEvent.click(rightChevron); } );
    const incrementedDate = new Date(initialDate.getTime() + MILLIS_A_DAY);

    await waitFor(() => expect(getByText(formatTime(incrementedDate, 'full'))).toBeInTheDocument());

  });

  // Test to check if date decrements when the left chevron is clicked
  test('decrements date when left chevron is clicked', async () => {
    const { getByText } = rendered;
    const leftChevron = getByText('Left');

    const initialDate = new Date();
    fireEvent.click(leftChevron);
    const decrementedDate = new Date(initialDate.getTime() - MILLIS_A_DAY);

    await waitFor(() => expect(getByText(formatTime(decrementedDate, 'full'))).toBeInTheDocument());
  });

  // Test to check if setShowNewProj is called with true when addProject is called
  test('calls setShowNewProj with true when addProject is called', () => {
    const { getByText } = rendered;

    const addProjectButton = getByText('New Project');
    fireEvent.click(addProjectButton);

    expect(setShowNewProj).toHaveBeenCalledWith(true);
  });
});