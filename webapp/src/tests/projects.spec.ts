import { test, Page, expect } from '@playwright/test';

// Helper function to login
async function login(page: Page) {
    await page.goto('/');
    await page.click('text=Log In');
    const nameField = await page.locator('input[name="name"], input[name="username"]').first();
    await nameField.fill('sample@sample.com');
    await page.fill('input[name="password"]', 'Sample123!');
    await page.click('button[type="submit"]');
    await expect(page).toHaveURL('/');
    await expect(page.locator('text=Log Out')).toBeVisible();
}

test.describe('Tests for the Projects page', () => {
    let page: Page;

    // Perform login once before all tests
    test.beforeAll(async ({ browser }) => {
        const context = await browser.newContext(); // Create a new browser context
        page = await context.newPage(); // Create a new page within the context

        // Perform login
        await login(page);
    });

    test('Navigation buttons shown', async () => {
        await page.goto('/projects');
        await expect(page.locator('text=Tasks')).toBeVisible();
        await expect(page.locator('text=Projects')).toHaveCount(2);
    });

    test('Click New Project and add a Project', async () => {
        await page.goto('/projects');
        await page.click('text=New Project');
        await expect(page.locator('text=Confirm')).toBeVisible();
        await expect(page.locator('text=Cancel')).toBeVisible();

        const mockProjectName = 'Mock' + Date.now();
        const mockProjectDescription = 'Lorem Ipsum'
        await page.fill('input[name="name"]', mockProjectName);
        await page.fill('textarea[name="description"]', mockProjectDescription);
        await page.click('text=Confirm');

        await page.reload()
        await expect(page.locator('text=' + mockProjectName)).toBeVisible();
    });

    test('Click New Project and cancel', async () => {
        await page.goto('/projects');
        await page.click('text=New Project');
        await expect(page.locator('text=Confirm')).toBeVisible();
        await expect(page.locator('text=Cancel')).toBeVisible();

        const mockProjectName = 'Mock' + Date.now();
        await page.fill('input[name="name"]', mockProjectName);
        await page.click('text=Cancel');

        page.reload()
        await expect(page.locator('text=' + mockProjectName)).not.toBeVisible();
    });

    test('Opens and closes edit menu', async () => {
        await page.goto('/projects');
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

    test('Opens edit menu on the project and update it', async () => {
        await page.goto('/projects');
        const firstMenu = page.locator('img[alt="Menu"]').first();
        await firstMenu.click();
        await expect(page.locator('text=Edit')).toBeVisible();
        await expect(page.locator('text=Delete')).toBeVisible();
        await expect(page.locator('text=Cancel')).toBeVisible();

        await page.click('text=Edit');
        await expect(page.locator('text=Delete')).not.toBeVisible();
        await expect(page.locator('text=Confirm')).toBeVisible();
        await expect(page.locator('text=Cancel')).toBeVisible();

        const mockProjectName = 'Mock' + Date.now();
        const mockProjectDescription = 'Lorem Ipsum'
        await page.fill('input[name="name"]', mockProjectName);
        await page.fill('textarea[name="description"]', mockProjectDescription);
        await page.click('text=Confirm');

        page.reload()
        await expect(page.locator('text=' + mockProjectName)).toBeVisible();
    });

    test('Opens edit menu on the project and cancel', async () => {
        await page.goto('/projects');
        const firstMenu = page.locator('img[alt="Menu"]').first();
        await firstMenu.click();
        await expect(page.locator('text=Edit')).toBeVisible();
        await expect(page.locator('text=Delete')).toBeVisible();
        await expect(page.locator('text=Cancel')).toBeVisible();

        await page.click('text=Edit');
        await expect(page.locator('text=Delete')).not.toBeVisible();
        await expect(page.locator('text=Confirm')).toBeVisible();
        await expect(page.locator('text=Cancel')).toBeVisible();

        const mockProjectName = 'Mock' + Date.now();
        const mockProjectDescription = 'Lorem Ipsum'
        await page.fill('input[name="name"]', mockProjectName);
        await page.fill('textarea[name="description"]', mockProjectDescription);
        await page.click('text=Cancel');

        page.reload()
        await expect(page.locator('text=' + mockProjectName)).not.toBeVisible();
    });

    test('Create a new project, open its menu, and delete it.', async () => {
        await page.goto('/projects');
        await page.click('text=New Project');
        await expect(page.locator('text=Confirm')).toBeVisible();
        await expect(page.locator('text=Cancel')).toBeVisible();

        const mockProjectName = 'Mock' + Date.now();
        const mockProjectDescription = 'Lorem Ipsum'
        await page.fill('input[name="name"]', mockProjectName);
        await page.fill('textarea[name="description"]', mockProjectDescription);
        await page.click('text=Confirm');

        page.reload()
        await expect(page.locator('text=' + mockProjectName)).toBeVisible();

        const lastMenu = page.locator('img[alt="Menu"]').last();
        await lastMenu.click();
        await expect(page.locator('text=Delete')).toBeVisible();
        await page.click('text=Delete');
        await expect(page.locator('text=' + mockProjectName)).not.toBeVisible();

    });

    test.afterAll(async () => {
        await page.close(); // Close the page
    });
});