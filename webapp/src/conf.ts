export const ROOT = 'http://localhost:9000/api'
export const TASK_API = ROOT + '/task'
export const PROJECT_API = ROOT + '/project'

export type TaskData = {
    id: number,
    project: string,
    task: string,
    duration: null | number, 
    status: string,
    date: string
}

export type ProjectData = {
    id: string,
    title: string,
    duration: string,
    completedAt: string | null,
    due: string,
    priority: 'low' | 'medium' | 'high',
    description: string
}