/**
 * Поставить лайк посту
 */
function likePost() {
    fetch('/posts/${post.id}/like', { method: 'PUT' })
        .then(response => location.reload());
}

/**
 * Удалить пост
 */
function deletePost() {
    if (confirm('Вы уверены, что хотите удалить этот пост?')) {
        fetch('/posts/${post.id}', { method: 'DELETE' })
            .then(response => window.location.href = '/posts');
    }
}

/**
 * Добавить комментарий
 */
function addComment() {
    const text = document.getElementById('commentText').value;
    fetch('/posts/${post.id}/comments', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ text })
    }).then(response => location.reload());
}

/**
 * Редактировать комментарий
 */
function editComment(button) {
    const cardBody = button.parentElement;
    const text = cardBody.querySelector('.card-text').innerText;
    cardBody.innerHTML = `
                <textarea class="form-control">${text}</textarea>
                <button class="btn btn-sm btn-success mt-2" onclick="saveComment(this)">Сохранить</button>
            `;
}

/**
 * Сохранить комментарий
 */
function saveComment(button) {
    const cardBody = button.parentElement;
    const text = cardBody.querySelector('textarea').value;
    const commentId = button.closest('.card').dataset.commentId;
    fetch('/comments/' + commentId, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ text })
    }).then(response => location.reload());
}

/**
 * Удалить комментарий
 */
function deleteComment(button) {
    if (confirm('Вы уверены, что хотите удалить этот комментарий?')) {
        const commentId = button.closest('.card').dataset.commentId;
        fetch('/comments/' + commentId, { method: 'DELETE' })
            .then(response => location.reload());
    }
}