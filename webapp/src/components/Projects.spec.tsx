import { render, fireEvent, act } from '@testing-library/react';
import Projects, { ProjectBlock, NewProjectModal } from './Projects';
import { ProjectData } from '../conf';

// Mock projects data
const mockDate = new Date();
const mockProjects: ProjectData[] = [
    {
        id: 1,
        category: { id: 1, name: 'Category' },
        name: 'Project 1',
        description: 'Sample description 1',
        status: 'STARTED',
        priority: 'LOW',
        startTime: mockDate,
        endTime: mockDate,
        dueTime: mockDate,
        estimatedHr: 1,
        dailyHr: 1,
        createdAt: mockDate,
        updatedAt: mockDate
    }, {
        id: 2,
        category: { id: 1, name: 'Category' },
        name: 'Project 2',
        description: 'Sample description 2',
        status: 'COMPLETED',
        priority: 'HIGH',
        startTime: mockDate,
        endTime: mockDate,
        dueTime: mockDate,
        estimatedHr: 10,
        dailyHr: 10,
        createdAt: mockDate,
        updatedAt: mockDate
    }
];
const projectRequest = { ...mockProjects[0], name: 'Updated Project' };

// Mock fetch responses
global.fetch = jest.fn((url) => {

    if (url.includes('/get')) {
        return Promise.resolve({
            ok: true,
            json: () => Promise.resolve(mockProjects),
        });

    } else if (url.includes('/add')) {
        return Promise.resolve({
            ok: true,
            json: () => Promise.resolve(mockProjects[0])
        });

    } else if (url.includes('/update')) {
        return Promise.resolve({
            ok: true,
            json: () => Promise.resolve(projectRequest)
        });

    } else if (url.includes('/delete')) {
        return Promise.resolve({ ok: true });
    }

    throw new Error(`Unexpected fetch call to ${url}`);

}) as jest.Mock;

describe('Projects Component', () => {

    let rendered: any;

    beforeEach(() => {
        rendered = render(<Projects />);
    });

    test('renders projects correctly', async () => {

        const { findByText, getByText } = rendered;

        // Wait for projects to be loaded
        await findByText('Project 1');

        // Assert that projects are rendered
        expect(getByText('Project 1')).toBeInTheDocument();
        expect(getByText('Project 2')).toBeInTheDocument();
    });

    test('open and close NewProjectModal', async () => {
        const { findByText, getByText, queryByText } = rendered;

        // Wait for projects to be loaded
        await findByText('Projects');

        // Click on New Project button
        fireEvent.click(getByText('New Project'));

        // Assert that NewProjectModal is displayed
        expect(getByText('Confirm')).toBeInTheDocument();

        // Click on Cancel button in NewProjectModal
        fireEvent.click(getByText('Cancel'));

        // Assert that NewProjectModal is closed
        expect(queryByText('Confirm')).not.toBeInTheDocument();
    });

});

describe('ProjectBlock Component', () => {

    let rendered: any;

    beforeEach(() => {
        rendered = render(<ProjectBlock data={mockProjects[0]} editor={() => {}} remover={() => {}} />);
    });

    test('renders project block correctly', () => {
        const { getByText } = rendered;

        // Assert that project data is rendered correctly
        expect(getByText('Project 1')).toBeInTheDocument();
        expect(getByText('Sample description 1')).toBeInTheDocument();
    });

});

describe('NewProjectModal Component', () => {

    let rendered: any;
    const mockProjectSetter = jest.fn();

    beforeEach(() => {
        rendered = render(<NewProjectModal data={mockProjects[0]} cancelHandler={() => {}} projectSetter={mockProjectSetter} />);
    });

    test('renders modal correctly', () => {

        const { container } = rendered;

        expect(container.querySelector('input[name="categoryId"]')).toBeInTheDocument();
        expect(container.querySelector('input[name="name"]')).toBeInTheDocument();
        expect(container.querySelector('input[name="dueTime"]')).toBeInTheDocument();
        expect(container.querySelector('input[name="estimatedHr"]')).toBeInTheDocument();
        expect(container.querySelector('input[name="dailyHr"]')).toBeInTheDocument();
        expect(container.querySelector('select[name="priority"]')).toBeInTheDocument();
        expect(container.querySelector('textarea[name="description"]')).toBeInTheDocument();

    });

    test('submits form with correct data', async () => {

        const { getByText } = rendered;

        // Submit the form
        fireEvent.click(getByText('Confirm'));

        // Wait for API call to complete
        await act(async () => {
            await Promise.resolve();
        });

        // Assert that projectSetter was called with the correct project data
        expect(mockProjectSetter).toHaveBeenCalledWith(projectRequest);
    });

});
