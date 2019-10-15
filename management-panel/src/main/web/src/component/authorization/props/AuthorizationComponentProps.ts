import {History} from 'history'

export interface AuthorizationComponentProps {
    onAuthorize: (history: History, token: string) => void
}
