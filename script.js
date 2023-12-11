const apiUrl = 'https://api.spoonacular.com/recipes/complexSearch';
let authToken = null;


async function login() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch(`${apiUrl}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }),
        });

        if (response.ok) {
            const data = await response.json();
            authToken = data.token;

            // Hide login section and show recipe search section
            document.getElementById('authSection').style.display = 'none';
            document.getElementById('recipeSection').style.display = 'block';
        } else {
            console.error('Login failed');
        }
    } catch (error) {
        console.error('Error during login:', error);
    }
}
async function searchRecipes() {
    const searchInput = document.getElementById('searchInput').value;

    try {
        const response = await fetch(`${apiUrl}/search?query=${searchInput}`, {
            headers: {
                'Authorization': `Bearer ${authToken}`,
            },
        });

        if (response.ok) {
            const data = await response.json();
            displayResults(data);
        } else {
            console.error('Recipe search failed');
        }
    } catch (error) {
        console.error('Error fetching data:', error);
    }
}


function displayResults(results) {
    const resultsContainer = document.getElementById('results');
    //clear the previous result
    resultsContainer.innerHTML = ''; 

    if (results.length === 0) {
        resultsContainer.innerHTML = '<p>No recipes found</p>';
        return;
    }

    results.forEach(recipe => {
        const recipeCard = document.createElement('div');
        recipeCard.classList.add('recipe-card');

        const title = document.createElement('h2');
        title.textContent = recipe.title;

        const ingredients = document.createElement('p');
        ingredients.textContent = `Ingredients: ${recipe.ingredients.join(', ')}`;

        const instructions = document.createElement('p');
        instructions.textContent = `Instructions: ${recipe.instructions}`;

        recipeCard.appendChild(title);
        recipeCard.appendChild(ingredients);
        recipeCard.appendChild(instructions);

        resultsContainer.appendChild(recipeCard);
    });
}



