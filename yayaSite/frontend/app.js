// =============================================
// YAYASWRLD — app.js
// =============================================

const API_BASE = 'http://localhost:8080/api'; // Changer en prod

// ---- CURSEUR CUSTOM ----
const cursor    = document.getElementById('cursor');
const cursorDot = document.getElementById('cursorDot');

if (cursor && cursorDot) {
  let mouseX = 0, mouseY = 0;
  let curX = 0, curY = 0;

  document.addEventListener('mousemove', e => {
    mouseX = e.clientX;
    mouseY = e.clientY;
    cursorDot.style.left = mouseX + 'px';
    cursorDot.style.top  = mouseY + 'px';
  });

  // Curseur principal avec lerp (lag fluide)
  function animateCursor() {
    curX += (mouseX - curX) * 0.12;
    curY += (mouseY - curY) * 0.12;
    cursor.style.left = curX + 'px';
    cursor.style.top  = curY + 'px';
    requestAnimationFrame(animateCursor);
  }
  animateCursor();

  // Agrandir le curseur sur les éléments interactifs
  document.querySelectorAll('a, button, input, textarea').forEach(el => {
    el.addEventListener('mouseenter', () => cursor.style.transform = 'translate(-50%,-50%) scale(2)');
    el.addEventListener('mouseleave', () => cursor.style.transform = 'translate(-50%,-50%) scale(1)');
  });
}

// ---- NAVBAR SCROLL ----
const navbar = document.getElementById('navbar');
const navLinks = document.querySelectorAll('.nav-link');

window.addEventListener('scroll', () => {
  navbar.classList.toggle('scrolled', window.scrollY > 50);
  updateActiveNav();
});

function updateActiveNav() {
  const sections = document.querySelectorAll('section[id]');
  const scrollY  = window.scrollY + 120;

  sections.forEach(section => {
    const top    = section.offsetTop;
    const height = section.offsetHeight;
    const id     = section.getAttribute('id');

    if (scrollY >= top && scrollY < top + height) {
      navLinks.forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('href') === `#${id}`) link.classList.add('active');
      });
    }
  });
}

// ---- MENU MOBILE ----
const navToggle = document.getElementById('navToggle');
const navLinksContainer = document.getElementById('navLinks');

navToggle?.addEventListener('click', () => {
  navLinksContainer.classList.toggle('open');
});

// Fermer le menu au clic sur un lien
navLinksContainer?.querySelectorAll('a').forEach(link => {
  link.addEventListener('click', () => navLinksContainer.classList.remove('open'));
});

// ---- REVEAL ON SCROLL (Intersection Observer) ----
const revealObserver = new IntersectionObserver(
  entries => entries.forEach(e => {
    if (e.isIntersecting) {
      e.target.classList.add('visible');
      revealObserver.unobserve(e.target);
    }
  }),
  { threshold: 0.1 }
);

document.querySelectorAll('.reveal').forEach(el => revealObserver.observe(el));

// ---- DESIGNS — FETCH DEPUIS LE BACKEND JAVA ----
async function loadDesigns() {
  const grid = document.getElementById('designsGrid');
  if (!grid) return;

  try {
    const res  = await fetch(`${API_BASE}/designs`);
    if (!res.ok) throw new Error('Backend non disponible');
    const data = await res.json();

    if (!data.length) return; // Garder les placeholders si vide

    grid.innerHTML = '';
    data.forEach((design, i) => {
      const delay = ['', 'delay-1', 'delay-2', 'delay-3'][i % 4];
      const card  = document.createElement('div');
      card.className = `design-card reveal ${delay}`;
      card.innerHTML = `
        <div class="design-img-wrap">
          <img src="${design.imageUrl}" alt="${design.title}" loading="lazy" />
        </div>
        <div class="design-info">
          <span class="design-cat">${design.category}</span>
          <h3 class="design-title">${design.title}</h3>
        </div>
      `;
      grid.appendChild(card);
      revealObserver.observe(card);
    });

  } catch (err) {
    // Backend pas encore lancé — garder les placeholders silencieusement
    console.info('Backend non connecté — affichage des placeholders');
  }
}

loadDesigns();

// ---- FORMULAIRE CONTACT ----
const contactForm = document.getElementById('contactForm');
const formFeedback = document.getElementById('formFeedback');
const submitBtn = document.getElementById('submitBtn');

contactForm?.addEventListener('submit', async e => {
  e.preventDefault();

  const payload = {
    name:    document.getElementById('contactName').value.trim(),
    email:   document.getElementById('contactEmail').value.trim(),
    subject: document.getElementById('contactSubject').value.trim(),
    message: document.getElementById('contactMessage').value.trim(),
  };

  submitBtn.textContent = 'Envoi en cours...';
  submitBtn.disabled = true;
  formFeedback.className = 'form-feedback';
  formFeedback.textContent = '';

  try {
    const res = await fetch(`${API_BASE}/contact`, {
      method:  'POST',
      headers: { 'Content-Type': 'application/json' },
      body:    JSON.stringify(payload),
    });

    if (res.ok) {
      formFeedback.className = 'form-feedback success';
      formFeedback.textContent = '✓ Message envoyé ! Yaya te répondra bientôt.';
      contactForm.reset();
    } else {
      throw new Error();
    }
  } catch {
    formFeedback.className = 'form-feedback error';
    formFeedback.textContent = '✗ Erreur lors de l\'envoi. Réessaie ou contact directement sur Instagram.';
  } finally {
    submitBtn.textContent = 'Envoyer le message';
    submitBtn.disabled = false;
  }
});

// ---- FORMULAIRE MERCH NOTIFY ----
const notifyForm    = document.getElementById('notifyForm');
const merchConfirm  = document.getElementById('merchConfirm');

notifyForm?.addEventListener('submit', async e => {
  e.preventDefault();
  const email = document.getElementById('notifyEmail').value.trim();

  try {
    await fetch(`${API_BASE}/notify`, {
      method:  'POST',
      headers: { 'Content-Type': 'application/json' },
      body:    JSON.stringify({ email }),
    });
  } catch {
    // Silencieux si backend down
  }

  notifyForm.style.display = 'none';
  merchConfirm.style.display = 'block';
});
