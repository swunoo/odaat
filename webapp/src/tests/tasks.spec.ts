import { test, Page, expect } from '@playwright/test';
import { formatTime, getDateFormatted, MILLIS_A_DAY } from '../utils';

// Helper function to login
async function login(page: Page) {
    await page.goto('/');
    await page.click('text=Log In');
    await page.fill('input[name="username"]', 'sample@sample.com');
    await page.fill('input[name="password"]', 'Sample123!');
    await page.click('button[type="submit"]');
    await expect(page).toHaveURL('/');
    await expect(page.locator('text=Log Out')).toBeVisible();
}

test.describe('Tests for the Tasks page', () => {
    let page: Page;

    // Perform login once before all tests
    test.beforeAll(async ({ browser }) => {
        const context = await browser.newContext(); // Create a new browser context
        page = await context.newPage(); // Create a new page within the context

        // Perform login
        await login(page);
    });

    test('Navigation buttons shown and work', async () => {
        await page.goto('/tasks');
        await expect(page.locator('text=Tasks')).toBeVisible();
        await expect(page.locator('text=Projects')).toBeVisible();

        await page.click('text=Tasks');
        await expect(page).toHaveURL(/tasks/);
        await page.click('text=Projects')
        await expect(page).toHaveURL(/projects/);

    });

    test('Current date is shown', async () => {
        await page.goto('/tasks');
        const currentDate = formatTime(new Date(), 'full');
        await expect(page.locator('text='+currentDate)).toBeVisible();
    });

    test('Click New Task and add a Task', async () => {
        await page.goto('/tasks');
        await page.click('text=New Task');
        await expect(page.locator('text=Confirm')).toBeVisible();
        await expect(page.locator('text=Cancel')).toBeVisible();

        const mockTaskName = 'Mock' + Date.now();
        const mockTaskDate = getDateFormatted(new Date());
        const mockTaskTime = '01:03'
        const mockTaskDuration = (Math.random() * 10).toFixed(2)
        await page.fill('#task-input-task', mockTaskName);
        await page.fill('#task-input-startTimeDate', mockTaskDate);
        await page.fill('#task-input-startTimeTime', mockTaskTime);
        await page.fill('#task-input-duration', mockTaskDuration);
        await page.click('text=Confirm');

        page.reload()
        await expect(page.locator('text=Cancel')).not.toBeVisible();
        await expect(page.locator('text=' + mockTaskName)).toBeVisible();
    });

    test('Click New Task and cancel', async () => {
        await page.goto('/tasks');
        await page.click('text=New Task');
        await expect(page.locator('text=Confirm')).toBeVisible();
        await expect(page.locator('text=Cancel')).toBeVisible();

        const mockTaskName = 'Mock' + Date.now();
        const mockTaskDate = getDateFormatted(new Date());
        await page.fill('#task-input-task', mockTaskName);
        await page.fill('#task-input-startTimeDate', mockTaskDate);
        await page.click('text=Cancel');

        await expect(page.locator('text=Cancel')).not.toBeVisible();
        await expect(page.locator('text=' + mockTaskName)).not.toBeVisible();
    });

    test('Opens and closes edit menu', async () => {
        await page.goto('/tasks');
        const firstMenu = page.locator('img[alt="Menu"]').first();
        await firstMenu.click();
        await expect(page.locator('text=Edit')).toBeVisible();
        await expect(page.locator('text=Delete')).toBeVisible();
        await expect(page.locator('text=Cancel')).toBeVisible();

        await firstMenu.click();
        await expect(page.locator('text=Edit')).not.toBeVisible();
        await expect(page.locator('text=Delete')).not.toBeVisible();
        await expect(page.locator('text=Cancel')).not.toBeVisible();

        await firstMenu.click();
        await page.click('text=Cancel');
        await expect(page.locator('text=Edit')).not.toBeVisible();
        await expect(page.locator('text=Delete')).not.toBeVisible();
        await expect(page.locator('text=Cancel')).not.toBeVisible();
    });

    test('Opens edit menu on the task and update it', async () => {
        await page.goto('/tasks');
        const firstMenu = page.locator('img[alt="Menu"]').first();
        await firstMenu.click();
        await expect(page.locator('text=Edit')).toBeVisible();
        await expect(page.locator('text=Delete')).toBeVisible();
        await expect(page.locator('text=Cancel')).toBeVisible();

        await page.click('text=Edit');
        await expect(page.locator('text=Delete')).not.toBeVisible();
        await expect(page.locator('text=Confirm')).toBeVisible();
        await expect(page.locator('text=Cancel')).toBeVisible();

        const mockTaskName = 'Mock' + Date.now();
        const mockTaskDate = getDateFormatted(new Date());
        const mockTaskTime = '01:03'
        const mockTaskDuration = (Math.random() * 10).toFixed(2)
        await page.fill('#task-input-task', mockTaskName);
        await page.fill('#task-input-startTimeDate', mockTaskDate);
        await page.fill('#task-input-startTimeTime', mockTaskTime);
        await page.fill('#task-input-duration', mockTaskDuration);
        await page.click('text=Confirm');

        page.reload()
        await expect(page.locator('text=Cancel')).not.toBeVisible();
        await expect(page.locator('text=' + mockTaskName)).toBeVisible();
    });

    test('Opens edit menu on the task and cancel it', async () => {
        await page.goto('/tasks');
        const firstMenu = page.locator('img[alt="Menu"]').first();
        await firstMenu.click();

        await page.click('text=Edit');
        await expect(page.locator('text=Delete')).not.toBeVisible();
        await expect(page.locator('text=Confirm')).toBeVisible();
        await expect(page.locator('text=Cancel')).toBeVisible();

        const mockTaskName = 'Mock' + Date.now();
        const mockTaskDate = getDateFormatted(new Date());
        await page.fill('#task-input-task', mockTaskName);
        await page.fill('#task-input-startTimeDate', mockTaskDate);
        await page.click('text=Cancel');

        await expect(page.locator('text=Cancel')).not.toBeVisible();
        await expect(page.locator('text=' + mockTaskName)).not.toBeVisible();
    });

    test('Create a new task, open its menu, and delete it.', async () => {
        await page.goto('/tasks');
        await page.click('text=New Task');
        await expect(page.locator('text=Confirm')).toBeVisible();
        await expect(page.locator('text=Cancel')).toBeVisible();

        const mockTaskName = 'Mock' + Date.now();
        const mockTaskDate = getDateFormatted(new Date());
        const mockTaskTime = '01:03'
        const mockTaskDuration = (Math.random() * 10).toFixed(2)
        await page.fill('#task-input-task', mockTaskName);
        await page.fill('#task-input-startTimeDate', mockTaskDate);
        await page.fill('#task-input-startTimeTime', mockTaskTime);
        await page.fill('#task-input-duration', mockTaskDuration);
        await page.click('text=Confirm');

        page.reload()
        await expect(page.locator('text=Cancel')).not.toBeVisible();
        await expect(page.locator('text=' + mockTaskName)).toBeVisible();

        const lastMenu = page.locator('img[alt="Menu"]').last();
        await lastMenu.click();
        await expect(page.locator('text=Delete')).toBeVisible();
        await page.click('text=Delete');
        await expect(page.locator('text=' + mockTaskName)).not.toBeVisible();

    });

    test('Update completion checkbox', async () => {
        await page.goto('/tasks');
        let completion = page.locator("input[type='checkbox']").first();

        const initialState = await completion.isChecked();

        await completion.click();
        await page.reload()
        completion = page.locator("input[type='checkbox']").first();
        expect(await completion.isChecked()).toBe(!initialState);

        await completion.click();
        await page.reload()
        completion = page.locator("input[type='checkbox']").first();
        expect(await completion.isChecked()).toBe(initialState);

    });


    test.afterAll(async () => {
        await page.close(); // Close the page
    });
});