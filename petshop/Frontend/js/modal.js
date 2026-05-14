

function abrirProdutos() {
  const overlay = document.getElementById("overlayP");
  overlay.classList.remove("hidden");
  overlay.classList.add("flex");
}

function fecharProdutos() {
  const overlay = document.getElementById("overlayP");
  overlay.classList.remove("hidden");
  overlay.classList.add("flex");
}

function abrir() { 
        document.getElementById('overlay').classList.add('flex');
        document.getElementById('overlay').classList.remove('hidden');
    }
    
    function fechar() { 
        document.getElementById('overlay').classList.add('hidden');
        document.getElementById('overlay').classList.remove('flex');
    }
    
    document.getElementById('overlay').addEventListener('click', function(e) {
        if (e.target === this) fechar();
    });



