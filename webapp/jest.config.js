export default {
    testEnvironment: "jsdom",
    moduleNameMapper: {
      '\\.(jpg|jpeg|png|gif|webp|svg)$': '<rootDir>/src/__fileMock.ts'
    },
    testMatch: ['<rootDir>/src/components/*.spec.tsx'],
    transform: {
      "^.+\\.tsx?$": "ts-jest",
    },
    setupFilesAfterEnv: ["<rootDir>/jest.setup.ts"],
  };  