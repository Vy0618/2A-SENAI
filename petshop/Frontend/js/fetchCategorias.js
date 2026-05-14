function abrirCategoria(id){
    window.location.href = `produtos.html?categoria=${id}`;
}

// CATEGORIAS CARD ----------------------------------------------------------------

fetch("http://localhost:8080/categorias")
    .then(response => response.json())
    .then(data => {
        const tabela = document.getElementById("sectionscategorias");
        const contador = document.getElementById("cat-count");
        
        tabela.innerHTML = "";
        
        // Atualiza contador
        if (contador) {
            contador.textContent = `${data.length} categorias`;
        }

        data.forEach((categoria, index) => {
            const numero = String(index + 1).padStart(2, '0');
            
            const card = document.createElement('div');
            card.className = 'bg-white p-8 relative transition-all duration-300 cursor-pointer hover:bg-sage-pale';
            card.onclick = () => abrirCategoria(categoria.id_categoria);
            
            card.innerHTML = `
                <div class="text-[0.6rem] text-ink-faint tracking-[0.15em] mb-4 font-dm">
                    ${numero}
                </div>
                <h3 class="font-cormorant text-[1.2rem] font-normal text-ink mb-2">
                    ${categoria.nome}
                </h3>
                <p class="text-[0.78rem] text-ink-mid leading-[1.75] mb-6 font-light font-dm">
                    ${categoria.descricao}
                </p>
                <div class="flex items-center justify-between">
                    <span class="text-[0.68rem] text-sage tracking-[0.12em] uppercase font-dm font-normal hover:text-ink transition-colors duration-200">
                        Ver produtos →
                    </span>
                    <button onclick="event.stopPropagation(); excluir(${categoria.id_categoria}, this)" 
                        class="bg-none border-0 text-ink-faint cursor-pointer text-[0.8rem] p-1 transition-colors duration-200 hover:text-red-500 leading-none"
                        title="Excluir categoria">
                        <i class="fa-regular fa-trash-can"></i>
                    </button>
                </div>
            `;
            
            tabela.appendChild(card);
        });
    })
    .catch(error => console.error("Erro ao carregar categorias:", error));


// CATEGORIAS NAVBAR ----------------------------------------------------------------

fetch("http://localhost:8080/categorias")
    .then(response => response.json())
    .then(data => {
        const nav = document.getElementById("categoriasnav");
        nav.innerHTML = "";

        data.forEach(categoria => {
            const item = document.createElement('button');
            item.className = 'text-[0.7rem] font-normal tracking-[0.12em] uppercase text-ink-mid no-underline py-[0.7rem] px-0 inline-block border-b border-transparent transition-all duration-200 hover:text-ink hover:border-b-ink font-dm bg-transparent cursor-pointer';
            item.onclick = () => abrirCategoria(categoria.id_categoria);
            item.textContent = categoria.nome;
            
            nav.appendChild(item);
        });
    })
    .catch(error => console.error("Erro ao carregar navbar:", error));


// CATEGORIAS FOOTER ----------------------------------------------------------------

fetch("http://localhost:8080/categorias")
    .then(response => response.json())
    .then(data => {
        const footerList = document.getElementById("catfooter");
        footerList.innerHTML = "";

        data.forEach(categoria => {
            const li = document.createElement('li');
            li.className = 'mb-[0.6rem]';
            
            const link = document.createElement('a');
            link.href = `produtos.html?categoria=${categoria.id_categoria}`;
            link.className = 'text-ink-mid no-underline text-[0.8rem] font-light transition-colors duration-200 hover:text-ink font-dm';
            link.textContent = categoria.nome;
            
            li.appendChild(link);
            footerList.appendChild(li);
        });
    })
    .catch(error => console.error("Erro ao carregar footer:", error));