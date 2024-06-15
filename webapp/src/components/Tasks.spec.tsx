import { fireEvent, render } from '@testing-library/react';
import Tasks, { TaskContainer } from './Tasks';
import { formatTime, MILLIS_A_DAY } from '../utils';
import { ProjectData } from '../conf';

// Mock data, components and assets
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

jest.mock('./common', () => ({
  SvgChevronLeft: ({ clickHandler }: { clickHandler: () => void }) => <button onClick={clickHandler}>Left</button>,
  SvgChevronRight: ({ clickHandler }: { clickHandler: () => void }) => <button onClick={clickHandler}>Right</button>
}));

jest.mock('./TaskWrapper', () => ({
  TaskWrapper: ({ date, addProject, newProj }: any) => <div>{date.toString()} <span>{newProj?.name}</span> <button onClick={addProject}>New Project</button></div>
}));

jest.mock('../assets/calendarIcon.png', () => 'calendar-icon.png');

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

  beforeEach(() => {
    rendered = render(<Tasks />);
  });

    test('renders Navbar', () => {
      const { getByText } = rendered;
        expect(getByText('projects')).toBeInTheDocument();
        expect(getByText('tasks')).toBeInTheDocument();
    });

    test('shows NewProjectModal when setShowNewProj is called', () => {
      const { getByText, getByTestId } = rendered;

        fireEvent.click(getByText('New Project'));
        expect(getByTestId('new-project-modal')).toBeInTheDocument();
    });

    test('hides NewProjectModal when cancelHandler is called', () => {
        const { getByText, getByTestId, queryByTestId } = rendered;

        fireEvent.click(getByText('New Project'));
        expect(getByTestId('new-project-modal')).toBeInTheDocument();
        
        fireEvent.click(getByText('Cancel'));
        expect(queryByTestId('new-project-modal')).not.toBeInTheDocument();
    });

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

  beforeEach(() => {
    setShowNewProj = jest.fn();
    rendered = render(<TaskContainer newProj={mockProject} setShowNewProj={setShowNewProj} />);
  });

  test('renders correctly with all elements', () => {
    const { getByText, getByAltText } = rendered;

    expect(getByAltText('Change Date')).toBeInTheDocument();
    expect(getByText(formatTime(new Date(), 'full'))).toBeInTheDocument();
    expect(getByText('Test Project')).toBeInTheDocument();
  });

  test('increments date when right chevron is clicked', () => {
    const { getByText } = rendered;
    const rightChevron = getByText('Right');

    const initialDate = new Date();
    fireEvent.click(rightChevron);
    const incrementedDate = new Date(initialDate.getTime() + MILLIS_A_DAY);

    expect(getByText(formatTime(incrementedDate, 'full'))).toBeInTheDocument();
  });

  test('decrements date when left chevron is clicked', () => {
    const { getByText } = rendered;
    const leftChevron = getByText('Left');

    const initialDate = new Date();
    fireEvent.click(leftChevron);
    const decrementedDate = new Date(initialDate.getTime() - MILLIS_A_DAY);

    expect(getByText(formatTime(decrementedDate, 'full'))).toBeInTheDocument();
  });

  test('calls setShowNewProj with true when addProject is called', () => {
    const { getByText } = rendered;

    const addProjectButton = getByText('New Project');
    fireEvent.click(addProjectButton);

    expect(setShowNewProj).toHaveBeenCalledWith(true);
  });
});