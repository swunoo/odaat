export const ROOT = 'http://localhost:9000/api'
export const TASK_API = ROOT + '/task'
export const PROJECT_API = ROOT + '/project'
export const BACKLOG_API = ROOT + '/sync/backlog'

export type Priority = 'LOWEST' | 'LOW' | 'MEDIUM' | 'HIGH' | 'HIGHEST';

export type ProjectStatus = 'CREATED' | 'STARTED' | 'COMPLETED' | 'PAUSED' | 'STOPPED';

export type TaskStatus = 'GENERATED' | 'PLANNED' | 'COMPLETED';


export interface ProjectCategory {
    id: number,
    name: string
}

export interface ProjectData {
    id: number,
    category: ProjectCategory,
    name: string,
    description: string,
    status: ProjectStatus,
    priority: Priority,
    startTime: Date,
    endTime: Date,
    dueTime: Date,
    estimatedHr: number,
    dailyHr: number,
    createdAt: Date,
    updatedAt: Date
}

export interface TaskData {
    id: number,
    project: ProjectData,
    description: string,
    status: TaskStatus,
    priority: Priority,
    startTime: Date,
    durationHr: number,
    createdAt: Date,
    updatedAt: Date
}

export interface ProjectRequest {
    categoryId: number,
    name: string,
    description: string,
    status: ProjectStatus,
    priority: Priority,
    startTime: Date,
    endTime: Date,
    dueTime: Date,
    estimatedHr: number,
    dailyHr: number
}

export interface TaskRequest {
    projectId: number,
    description: string,
    status: TaskStatus,
    priority: Priority,
    startTime: Date,
    durationHr: number
}