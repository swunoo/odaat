import { render, fireEvent, waitFor } from '@testing-library/react';
import { TaskWrapper, TaskBlock } from './TaskWrapper';
import { ProjectData, TaskData } from "../conf";

// Mock data
const mockDate = new Date();
const mockProject: ProjectData = {
    id: 1,
    category: { id: 1, name: 'Category' },
    name: 'Project',
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
const mockTask: TaskData = {
    id: 1,
    project: mockProject,
    description: 'Test Task',
    status: 'PLANNED',
    priority: 'LOW',
    startTime: mockDate,
    durationHr: 1,
    createdAt: mockDate,
    updatedAt: mockDate
};

// Mock fetch responses
global.fetch = jest.fn((url) => {
    
    if (url.includes('/getIdName')) {
        return Promise.resolve({
            ok: true,
            json: () => Promise.resolve([mockProject]),
        });

    } else if (url.includes('/get')) {
        return Promise.resolve({
            ok: true,
            json: () => Promise.resolve([mockTask]),
        });

    } else if (url.includes('/update')) {
        return Promise.resolve({
            ok: true,
            json: () => Promise.resolve({...mockTask, description: 'Updated Task'})
        });

    } else if (url.includes('/updateStatus')) {
        return Promise.resolve({ ok: true });

    } else if (url.includes('/delete')) {
        return Promise.resolve({ ok: true });
    }

    throw new Error(`Unexpected fetch call to ${url}`);

}) as jest.Mock;

describe('TaskWrapper Component', () => {
    let rendered: any;
    beforeEach(() => {
        rendered = render(<TaskWrapper project={mockProject} date={new Date()} addProject={() => { }} newProj={mockProject} />);
    });

    test('renders TaskWrapper component', async () => {
        const { getByText } = rendered;
        await waitFor(() => expect(getByText('Test Task')).toBeInTheDocument());
    });

    test('adds a new task', async () => {

        const { getByText, getAllByText } = rendered;

        await waitFor(() => expect(getByText('Test Task')).toBeInTheDocument());

        fireEvent.click(getByText('New Task'));
        expect(getAllByText('Task Name')).toHaveLength(1); // One new task should be added
    });

    test('deletes a task', async () => {
        const { getByText, getByAltText, queryByText } = rendered;

        await waitFor(() => expect(getByText('Test Task')).toBeInTheDocument());

        fireEvent.click(getByAltText('Menu'));
        fireEvent.click(getByText('Delete'));

        await waitFor(() => expect(queryByText('Test Task')).not.toBeInTheDocument());
    });
});

describe('TaskBlock Component', () => {

    let rendered: any;
    beforeEach(() => {
        const taskSetter = jest.fn();
        const remover = jest.fn();
        jest.clearAllMocks();
        rendered = render(<TaskBlock initData={mockTask} projects={[mockProject]} addProject={() => { }} remover={remover} taskSetter={taskSetter} isTaskPage={true} />);
    });

    test('edits a task', async () => {

        const { getByText, getByAltText, getByDisplayValue, queryByText } = rendered;

        expect(getByText('Test Task')).toBeInTheDocument()

        fireEvent.click(getByAltText('Menu'));
        fireEvent.click(getByText('Edit'));
        fireEvent.change(getByDisplayValue('Test Task'), { target: { value: 'Updated Task' } });
        fireEvent.click(getByText('Confirm'));

        await waitFor(() => {
            expect(getByText('Updated Task')).toBeInTheDocument()
            expect(queryByText('Test Task')).not.toBeInTheDocument()
        });
    });

    test('toggles task completion status', async () => {
        const { getByRole } = rendered;
        fireEvent.click(getByRole('checkbox'));
        // Assert if the checkbox is checked
        await waitFor(() => expect(getByRole('checkbox')).toBeChecked());
    });
});