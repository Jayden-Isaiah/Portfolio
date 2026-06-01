// =============================================
// YAYASWRLD — app.js (frontend only, Netlify Forms)
// =============================================

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

  function animateCursor() {
    curX += (mouseX - curX) * 0.12;
    curY += (mouseY - curY) * 0.12;
    cursor.style.left = curX + 'px';
    cursor.style.top  = curY + 'px';
    requestAnimationFrame(animateCursor);
  }
  animateCursor();

  document.querySelectorAll('a, button, input, textarea').forEach(el => {
    el.addEventListener('mouseenter', () => cursor.style.transform = 'translate(-50%,-50%) scale(2)');
    el.addEventListener('mouseleave', () => cursor.style.transform = 'translate(-50%,-50%) scale(1)');
  });
}

// ---- NAVBAR SCROLL ----
const navbar   = document.getElementById('navbar');
const navLinks = document.querySelectorAll('.nav-link');

window.addEventListener('scroll', () => {
  navbar?.classList.toggle('scrolled', window.scrollY > 50);
  updateActiveNav();
});

function updateActiveNav() {
  const sections = document.querySelectorAll('section[id]');
  const scrollY  = window.scrollY + 100;
  sections.forEach(section => {
    const id = section.getAttribute('id');
    if (scrollY >= section.offsetTop && scrollY < section.offsetTop + section.offsetHeight) {
      navLinks.forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('href') === `#${id}`) link.classList.add('active');
      });
    }
  });
}

// ---- MENU MOBILE ----
const navToggle         = document.getElementById('navToggle');
const navLinksContainer = document.getElementById('navLinks');

navToggle?.addEventListener('click', () => {
  navLinksContainer.classList.toggle('open');
});
navLinksContainer?.querySelectorAll('a').forEach(link => {
  link.addEventListener('click', () => navLinksContainer.classList.remove('open'));
});

// ---- REVEAL ON SCROLL ----
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

// ---- FORMULAIRE CONTACT (Netlify Forms) ----
// Netlify intercepte le POST automatiquement sur son infrastructure.
// Aucun backend requis — les messages arrivent dans Netlify Dashboard → Forms.
// Pour activer les notifications email : Netlify → Forms → contact → Settings → Email notifications.

const contactForm  = document.getElementById('contactForm');
const formFeedback = document.getElementById('formFeedback');
const submitBtn    = document.getElementById('submitBtn');

contactForm?.addEventListener('submit', async e => {
  e.preventDefault();

  submitBtn.textContent = 'Envoi en cours...';
  submitBtn.disabled    = true;
  formFeedback.className = 'form-feedback';
  formFeedback.textContent = '';

  // Encode les données du formulaire pour Netlify
  const formData = new FormData(contactForm);
  const body     = new URLSearchParams(formData).toString();

  try {
    const res = await fetch('/', {
      method:  'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body,
    });

    if (res.ok) {
      formFeedback.className   = 'form-feedback success';
      formFeedback.textContent = '✓ Message envoyé ! Yaya te répondra bientôt.';
      contactForm.reset();
    } else {
      throw new Error();
    }
  } catch {
    formFeedback.className   = 'form-feedback error';
    formFeedback.textContent = '✗ Erreur lors de l\'envoi. Contacte directement sur Instagram.';
  } finally {
    submitBtn.textContent = 'Envoyer le message';
    submitBtn.disabled    = false;
  }
});

// ---- FORMULAIRE MERCH NOTIFY (Netlify Forms) ----
const notifyForm   = document.getElementById('notifyForm');
const merchConfirm = document.getElementById('merchConfirm');

notifyForm?.addEventListener('submit', async e => {
  e.preventDefault();

  const formData = new FormData(notifyForm);
  const body     = new URLSearchParams(formData).toString();

  try {
    await fetch('/', {
      method:  'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body,
    });
  } catch {
    // Silencieux — Netlify gère l'erreur
  }

  notifyForm.style.display  = 'none';
  if (merchConfirm) merchConfirm.style.display = 'block';
});
