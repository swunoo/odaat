import { test, expect } from '@playwright/test';

test.describe('OAuth2 Authentication', () => {

  test('Should display about screen and login button', async ({ page }) => {
    await page.goto('/');
    await expect(page.locator('text=Log In')).toBeVisible();
  });

  test('Should redirect to OAuth2 provider on login button click', async ({ page }) => {
    await page.goto('/');
    await page.click('text=Log In');
    await expect(page).toHaveURL(/.auth0.com/);
  });

  test('Should login successfully', async ({ page }) => {
    await page.goto('/');
    await page.click('text=Log In');

    // Mocking OAuth2 login process
    await page.fill('input[name="username"]', 'sample@sample.com');
    await page.fill('input[name="password"]', 'Sample123!');
    await page.click('button[type="submit"]');

    // Redirect back to the application
    await expect(page).toHaveURL('/');
    await expect(page.locator('text=Log Out')).toBeVisible();
  });

  test('Shouldnot login successfully', async ({ page }) => {
    await page.goto('/');
    await page.click('text=Log In');

    // Mocking OAuth2 login process
    await page.fill('input[name="username"]', 'null@incorrect.com');
    await page.fill('input[name="password"]', 'Sample123!');
    await page.click('button[type="submit"]');

    // Redirect back to the application
    await expect(page).toHaveURL(/.auth0.com/);
  });

  test('Should logout successfully', async ({ page }) => {
    await page.goto('/');
    await page.click('text=Log In');
    const nameField = await page.locator('input[name="name"], input[name="username"]').first();
    await nameField.fill('sample@sample.com');
    await page.fill('input[name="password"]', 'Sample123!');
    await page.click('button[type="submit"]');

    await expect(page).toHaveURL('/');
    await expect(page.locator('text=Log Out')).toBeVisible();

    await page.goto('/');
    await page.click('text=Log Out');
    await expect(page.locator('text=Log In')).toBeVisible();

  });

});