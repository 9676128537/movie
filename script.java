document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('movie-review-form');
    const feedback = document.getElementById('form-feedback');
    const reviewsList = document.getElementById('reviews-list');

    // Load existing reviews from localStorage
    loadReviews();

    form.addEventListener('submit', (event) => {
        event.preventDefault();

        // Get form values
        const title = document.getElementById('movie-title').value.trim();
        const rating = parseInt(document.getElementById('movie-rating').value, 10);
        const reviewText = document.getElementById('movie-review').value.trim();

        // Validation
        if (!title || !rating || !reviewText) {
            feedback.textContent = 'All fields are required.';
            feedback.style.color = 'red';
            return;
        }

        if (rating < 1 || rating > 5) {
            feedback.textContent = 'Rating must be between 1 and 5.';
            feedback.style.color = 'red';
            return;
        }

        // Clear feedback
        feedback.textContent = '';

        // Create a new review object
        const newReview = {
            id: Date.now(),
            title,
            rating,
            reviewText
        };

        // Save to local storage
        saveReview(newReview);

        // Append the review to the reviews list
        appendReview(newReview);

        // Clear the form fields
        form.reset();
    });

    function loadReviews() {
        const reviews = JSON.parse(localStorage.getItem('reviews')) || [];
        reviews.forEach(review => appendReview(review));
    }

    function saveReview(review) {
        const reviews = JSON.parse(localStorage.getItem('reviews')) || [];
        reviews.push(review);
        localStorage.setItem('reviews', JSON.stringify(reviews));
    }

    function appendReview(review) {
        const reviewItem = document.createElement('li');
        reviewItem.className = 'review-item';
        reviewItem.dataset.id = review.id;
        reviewItem.innerHTML = `
            <h3>${review.title}</h3>
            <p>Rating: ${review.rating}</p>
            <p>${review.reviewText}</p>
            <button class="edit-button">Edit</button>
            <button class="delete-button">Delete</button>
        `;

        reviewsList.appendChild(reviewItem);

        // Add event listeners for edit and delete buttons
        reviewItem.querySelector('.edit-button').addEventListener('click', () => editReview(review.id));
        reviewItem.querySelector('.delete-button').addEventListener('click', () => deleteReview(review.id));
    }

    function editReview(id) {
        const reviews = JSON.parse(localStorage.getItem('reviews'));
        const review = reviews.find(r => r.id === id);

        document.getElementById('movie-title').value = review.title;
        document.getElementById('movie-rating').value = review.rating;
        document.getElementById('movie-review').value = review.reviewText;

        // Remove the review from local storage and DOM
        deleteReview(id);
    }

    function deleteReview(id) {
        let reviews = JSON.parse(localStorage.getItem('reviews'));
        reviews = reviews.filter(r => r.id !== id);
        localStorage.setItem('reviews', JSON.stringify(reviews));
        document.querySelector(`.review-item[data-id="${id}"]`).remove();
    }
});
