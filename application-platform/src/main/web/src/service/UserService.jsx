export function loadCurrentUser(onSuccess) {
    onSuccess({
        name: 'Антон Баширов',
        avatarUrl: 'https://www.gravatar.com/avatar/a20dc7a2bd7364e1392da265ae810ee6?s=800&d=identicon',
        role: 'Администратор',
        email: 'Anton.Bashirov@rt.ru',
        gitLabUrl: 'http://10.35.215.200/gitlab'
    });
}

export function loadUsers(onSuccess) {
    onSuccess([
        {
            name: 'Антон Баширов',
            avatarUrl: 'https://www.gravatar.com/avatar/a20dc7a2bd7364e1392da265ae810ee6?s=800&d=identicon',
            role: 'Администратор',
            email: 'Anton.Bashirov@rt.ru',
            gitLabUrl: 'http://10.35.215.200/gitlab'
        }
    ]);
}

export function saveUsers(users) {

}