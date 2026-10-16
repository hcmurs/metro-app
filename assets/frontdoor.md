# frontdoor: design tokens and rules

Platform: `metro-app`, the Android app. This file is the complete specification of the frontdoor layer of this platform: the README banner, the figures under it, their tokens, their layout and the rules for changing them. It does not cover the screens inside the app. Read it end to end before drawing or editing anything in `assets/`; nothing else is needed. The other platforms (`metro-fe`, `metro-be`) keep their own `frontdoor.md` in their own `assets` folder and share the colours, the logo and the rules of this file.

When a token or a figure changes, change its source in `assets/source/` and this file in the same commit.

## 1. What exists

| Figure | Source page | Item height | README place | Job |
|---|---|---|---|---|
| `hero` | `source/hero.html` | 540 | top, inside `<h1>` | the pitch in three words and one ticket |
| `journey` | `source/journey.html` | 540 | "From ticket to gate" | the passenger path in four steps |
| `roles` | `source/roles.html` | 520 | "One app, two roles" | who uses the app |
| `features-tickets`, `-gate`, `-news`, `-voice`, `-around`, `-account` | `source/features.html` (six items) | 448 | "Everything in the app", one `###` heading each | one tile per feature, two tiles per sheet |
| `tech-hero` | `source/tech.html` | 340 | top of "Under the hood" | banner of the engineering part |
| `tech-flow` | `source/tech.html` | 540 | "How it fits together" | the layers of the app and the backend |
| `tech-stack` | `source/tech.html` | 560 | "Built with" | the stack in six tiles |
| `tech-workflow` | `source/tech.html` | 800 | "Run it and ship a change" | two route lines: run it locally, ship a change |

- Shared files: `source/shared.css` (tokens, base components, and the poster tile and chip rules used by `features.html` and `tech.html`), `source/shared.js` (theme switch), `source/logo.png` (the logo).
- Every item renders to `assets/<id>.webp` (light) and `assets/<id>-dark.webp` (dark), 2400 px wide, height twice the item height.
- Item id equals file name. Keep the ids above; the README refers to them.

## 2. Signature

The six lines every source page repeats in its header comment.

| | |
|---|---|
| Concept | a product pitch told at poster scale: loud lowercase type, flat circles, one ticket, one route line |
| Shape language | flat circles that bleed off the edges, a 26px ticket with notches and a tear line, 32px tiles, 999px chips, a rounded route line with ring nodes; no outlines on cards, no gradients, no glow |
| Typeface | Montserrat 900 for names and headings, 500 for lines, 700 for chips |
| Palette logic | HCMC Metro blue and red do all the work on white; red is an accent shape or large type, never body text; the dark theme swaps roles on a solid blue ground |
| Presentation | big type left, one object right; steps follow one route line; roles split by one diagonal; features are paired tiles |
| Not | a phone mockup, a screenshot, a photograph, a formal panel, a metro signage board, an official-looking certificate, anything glossy |

## 3. Tokens

The layer uses the colours and the logo of HCMC Metro (HURC), and shapes and type that are this project's own.

### Colour

Every value was read from the live official site `https://hurc.vn` with a headless Edge on 2026-10-16 (computed styles) and from its logo file `https://hurc.vn/images/logo.png` (the two modal colours of its pixels). Re-measure before changing one: load the page in a headless browser and read the computed colour of the headings, the selected chip, the unselected chip and the footer.

| Token (`shared.css`) | Value | Origin | Use |
|---|---|---|---|
| `--blue` | `#00599D` | computed colour of the site's headings, selected chip and footer ground; modal colour of `logo.png` | titles, lines, primary tile, dark-theme ground |
| `--red` | `#E83A27` | modal colour of the arc in `logo.png` | accent shapes, last-stop dot, large type only |
| `--white` | `#FFFFFF` | computed page ground of the site | light ground, tiles, chips on blue |
| `--black` | `#000000` | computed body text colour of the site | body text |
| `--chip` | `#F5F5F6` | computed ground of the site's unselected chips | grey tile, light-theme image border |

Contrast, computed (WCAG ratio):

| Pair | Ratio | Allowed for |
|---|---|---|
| white on `#00599D`, `#00599D` on white | 7.20 | any text |
| `#00599D` on `#F5F5F6` | 6.61 | any text |
| black on white, black on `#F5F5F6` | 21.00, 19.27 | any text |
| `#E83A27` on white, white on `#E83A27` | 4.15 | text of 24px or more (or 19px bold) only |
| `#E83A27` on `#F5F5F6` | 3.80 | text of 24px or more (or 19px bold) only |
| `#E83A27` on `#00599D` | 1.74 | never text; shapes only |

There is no navy, no pastel ground and no gradient in this layer. Do not sample colours from screenshots.

### Logo

- Use only `source/logo.png` (2398 x 627, transparent), the file published on the official site. Never redraw, trace, recolour, outline or stretch it. Shown at 224 x 59 (hero).
- On the light ground it sits directly. On the blue ground it sits on a white tile with 8px radius and 12px by 16px padding (`shared.css` does this through `.logo` in the dark theme).
- Keep at least 40px of empty space around it.
- The logo and the colours belong to HURC. Confirm that publishing them from this repository is permitted before the repository is made public.

### Type

- Family: Montserrat, weights 500, 700 and 900, loaded from Google Fonts at render time (`<link>` in each source page). It is not HURC's typeface; it is chosen for energy. Never copy a font file into the repository. Fallback stack: `"Segoe UI", system-ui, sans-serif`.
- Headings are lowercase, weight 900, tracking -0.03em to -0.045em, ending in a period. Body text is weight 500. Chips are weight 700.
- Sizes in CSS pixels before the 2x render: hero beats 112, role name 64, figure title 56, tile title 46, step title 34, body 19 to 21, chip 16, ticket title 30 (weight 800), ticket caption 17. Do not go below 16. The item is shown near 830 px wide on GitHub, so 16 px prints at about 11 px; that is the floor.

### Shape

| Element | Value |
|---|---|
| Circles | flat, bleed off the item edge (`data-bleed`), never outlined |
| Tile | 32px radius, flat fill, no border, no shadow |
| Chip | 999px radius, 34px high, 16px side padding |
| Ticket | 26px radius, two notches of 17px radius, dashed tear line |
| Route line | 10px stroke, round caps, ring nodes 60px across with a 9px ring; the last stop is a filled red dot |
| Logo tile (dark only) | 8px radius |

Shadows are used only under the hero ticket (`drop-shadow(0 22px 28px rgba(0,0,0,.28))`).

### Theme

| | Light | Dark |
|---|---|---|
| Ground | `#FFFFFF` | `#00599D` |
| Title and text | blue, black | white |
| Large circle | blue | red |
| Small circle | red | white |
| Feature tiles | blue tile with white text, grey tile with blue title | white tile, grey tile |
| Route line | blue | white |
| Logo | direct | on a white tile |
| Image border (`--cardline`) | `#F5F5F6` | `#00599D` |

HURC defines no dark theme. The dark variant reuses the solid blue footer of the official site: blue ground, white tiles. On the blue ground a blue shape would vanish, so chips and text blocks sit on white tiles there. Themes are switched by `html[data-theme="dark"]`, set by `shared.js` from `?theme=dark`; use the roles in `shared.css` (`--paper`, `--ink`, `--title`, `--big`, `--small`, `--h1` to `--h3`, `--inv-bg`, `--inv-ink`, `--icon2-bg`, `--icon2-fg`) instead of hard-coded colours so one markup serves both themes.

## 4. Figure anatomy

All numbers are CSS pixels inside an item 1200 wide. `shared.css` zooms every `.item` by 2.

### hero (1200 x 540)

- Large circle 660, left 690, top 190, `--big`. Small circle 330, left 1000, top -150, `--small`.
- Logo at left 56, top 40.
- Three beats "buy.", "scan.", "go." stacked, left 52, top 142, weight 900, 112px, line height 0.94, tracking -0.045em. Colours: `--h1`, `--h2`, `--h3` (blue, red, blue in light; all white in dark).
- Ticket 500 x 310 at left 570, top 124, rotated -6deg, white, 26px radius, notches at x 270 top and bottom. Left stub 270 wide with a 3px dashed tear line in blue at 40% and a 190px decorative QR pattern (25 x 25 cells, three finder squares, seeded random cells; it is not a real code and must never encode data). Right part: a small route line (150 x 40: hollow start node, red end node with a white dot), the title "My ticket" (800, 30px, blue) and the caption "QR refreshes every 60 seconds" (500, 17px, black).

### journey (1200 x 540)

- Circles: 250 at left 1010, top -120 (`--big`); 320 at left -130, top 410 (`--small`).
- Title "from ticket to gate." left 56, top 44, 56px, weight 900, `--title`.
- One route path across the item (10px, `--title`), four nodes at x 150, 420, 690, 960 with alternating y (330, 290, 330, 290). Nodes 1 to 3 are white with a 9px `--title` ring and a blue numeral (26px, 900); node 4 is a red filled dot (r 34) with a white numeral.
- Stop texts 245 wide, left = node x minus 30. Odd stops sit above the line (class `up`, top 264, translated up by its own height), even stops below (top 342). Each stop: name (34px, 900, -0.03em) and one line (19px, 500, line height 1.35).
- Stops: choose, pay, show, scan. One line per stop, claims in section 7.

### roles (1200 x 520)

- The right half is a polygon `640 0, 100% 0, 100% 100%, 500 100%` filled with `--inv-bg` (blue in light, white in dark).
- Halves: left at x 56 and right at x 690, each 470 wide. Each half: an icon disc 96px (top margin 52) with a 52px glyph at stroke 6, the role name (900, 64px, -0.045em, top margin 22), a list (500, 21px, line height 1.3, 13px gap) with 11px dot bullets.
- Left disc is red with a white ticket glyph; right disc is white (blue in dark) with a blue (white in dark) scan-frame glyph. Left bullets red; right bullets follow `--inv-ink`.
- Roles: "passenger." and "station staff.".

### features (items 1200 x 448, two tiles each)

- Item padding 24, two tiles side by side with a 32px gap; each tile 400 high, 32px radius, padding 32px 36px, content clipped.
- Tile `a` (left): blue with white text, red accent circle, white chips with blue text. In dark it becomes white with a blue motif, blue title, black text, red accent circle and blue chips.
- Tile `b` (right): `#F5F5F6` with blue title and black text, blue accent circle, blue chips with white text. Unchanged in dark.
- Accent circle 250 at right -80, top -90 (`data-bleed`).
- Order inside a tile: motif (190 x 163, a 140 x 120 viewBox, bold 8px-stroke shapes in the tile's main colour with one red accent), title (900, 46px, -0.04em), one line (500, 19px, max 470 wide), optional chip row (34px high, 8px gap).

| Item id | Tile a | Tile b |
|---|---|---|
| `features-tickets` | buy tickets. (Single, Day, Student) | check the fare. |
| `features-gate` | show your QR. (Not used, Used, Expired) | scan at the gate. (ENTRY, EXIT) |
| `features-news` | read the news. | get notified. |
| `features-voice` | tell us. (Góp ý, Sự cố ứng dụng, Khác) | ask the assistant. |
| `features-around` | see the line. | around the metro. (Go!Bus, TTGT, Công dân số, TNGo) |
| `features-account` | two languages. (English, Tiếng Việt) | sign in with Google. (Passenger, Station staff) |

Chips carry only labels that exist in the app. The tile line for each is in `features.html`.

### tech figures (`source/tech.html`, four items)

The engineering part keeps the same signature: tiles, chips, circles, route line. Only the content is technical. Chips here are names of libraries, services and commands, taken from the build files and the workflow file.

- `tech-hero` (1200 x 340): large circle 520 at left 760, top -180 (`--big`); small circle 170 at left 640, top 230 (`--small`). Title "under" and "the hood." stacked, left 52, top 52, 96px, weight 900, line height 0.94, first line `--h1`, second `--h2`. One line of 21px text at left 56, top 258, 520 wide. Five white chips with blue text in a column at left 860, top 64, gap 10 (Kotlin, Jetpack Compose, Hilt, Retrofit, Android 8.0 and up).
- `tech-flow` (1200 x 540): circle 250 at left 1010, top -120. Title "how it fits together." at left 56, top 36, 56px. Three tiles 316 x 256 at top 122, left 56, 442, 828 (blue, grey, blue; white, grey, white in dark), title 42px, one 19px line, chips. A 10px line with a red dot (r 15) joins each pair, drawn only inside the 70px gaps. Under the first two tiles a 44px pill (left 56, top 394, 702 wide, grey in light, white in dark, blue 18px text, weight 700) says Hilt wires the layers together. At top 468 a row of chips, led by the bold label "straight from the phone:", in `--inv-bg` with `--inv-ink` text.
- `tech-stack` (1200 x 560): a grid of 3 x 2 tiles, padding 24, gap 32, checkerboard of blue and grey tiles, title 42px, chips only, accent circle 140 at right -50, top -60. Tiles: ui, wiring, on device, services, build, android. Keep every chip row clear of the accent circle (title row at the top, chips from 16px under it).
- `tech-workflow` (1200 x 800): two route lines built like `journey` (same path shape, nodes at x 150, 420, 690, 960, last node a red dot), the first at y 190, the second 40px lower than a 340px offset (path starts at y 570). Row titles "run it." (top 36) and "ship a change." (top 416) at 46px. Stop texts 245 wide, all at the same top under their line (226 and 606); each has a 34px name, a 19px line, and at most one command chip. Circles: 250 top right, 300 bottom left at left -170, top 690.
- Dark theme: tiles go white (grey stays grey), chips on the white tiles are blue; the route line and nodes are white.

## 5. README layout

Order, top to bottom:

1. `<h1 align="center">` holding the hero picture. Its alt text starts with "HCMC Metro app: buy, scan, go." and then describes what is drawn, including the ticket text.
2. One bold sentence: what the app does for a passenger and for staff.
3. One short paragraph in plain words.
4. `## From ticket to gate` with the journey, `## One app, two roles` with the roles.
5. `## Everything in the app`, with one `###` heading per feature sheet in the order of the table above (Tickets and fares, At the gate, News and alerts, Talk to us, Around the metro, Account and language).
6. `## Under the hood`: the engineering part, visible and illustrated, in this order: the `tech-hero` banner and one plain sentence; `### How it fits together` (`tech-flow`, a short paragraph, then a `<details>` block with the API paths); `### Built with` (`tech-stack`); `### Run it and ship a change` (`tech-workflow`, then in text: what you need, clone and build with fenced commands, the configuration key table, and the numbered steps to ship a change); `### Code layout` (the folder tree); then a `Design tokens` `<details>` block that links to this file. Nothing technical appears above this heading.
7. `## Contact`.

Rules for the markup:

- Each figure is `<picture><source media="(prefers-color-scheme: dark)" srcset="assets/<id>-dark.webp"><img src="assets/<id>.webp" alt="..." width="100%"></picture>`.
- Alt text is the text form of the figure: read every label in order. Chips and list items are all named.
- Above `## Under the hood` the pitch voice addresses a passenger in plain English: what you can do, not how it is built. Technical terms (Hilt, Retrofit, endpoints) stay in the engineering part, except where a figure must name a payment option the user sees. Inside it the voice addresses a developer in short, concrete sentences.
- Commands and configuration stay as copyable text in the README, never only in a figure and never inside `<details>`. Long reference lists (API paths) and the link to this file go in `<details>`.
- No licence line and no status block unless the owner asks.
- In a `<details>` block, leave a blank line after `<summary>` and before `</details>`; commands are fenced code.

## 6. Procedures

### Change the copy of a figure

1. Edit the text in the source page.
2. Re-prove the claim in section 7 and fix the table there.
3. Re-render the page, view both themes, update the alt text in the README.

### Add or change a feature tile

1. Check the code delivers the feature (section 7). If it does not, stop.
2. Add the tile to a sheet in `features.html` (two tiles per item; add a new item with a new id when all sheets are full) using classes `t a` or `t b` alternately, a motif drawn in `var(--m1)` and `var(--m2)`, a lowercase title ending in a period, one line, optional chips.
3. Add the item id to the table in section 4 and a `###` block to the README.
4. Render and look at both themes.

### Add a new kind of figure

1. Write a new source page that links `shared.css` and `shared.js`, sets one `.item` per image with a unique id and height, and starts with a signature header comment (section 2) and the render command.
2. Use only the tokens and roles in section 3; add a role to `shared.css` for a new colour need instead of a hex in the page.
3. Mark every shape that leaves the item box with `data-bleed`.
4. Add it to the tables in sections 1 and 4.

### Change a token

1. Re-measure the colour on the official site (section 3).
2. Edit `source/shared.css` and the tables here, re-render every page, view every image in both themes, recompute the contrast pairs.

### Render

The pipeline is the `frontdoor-illustrate` renderer; any tool that does the same steps works: serve `assets/` over HTTP, open each page with `?theme=light` and `?theme=dark`, screenshot each `.item` at 2x, round the corners (24 output px) and add a 3 output px border in `--cardline`, save lossless WebP named `<id>.webp` and `<id>-dark.webp` into `assets/`. With the skill:

```bash
uvx --from playwright --with pillow python -I <skill-dir>/scripts/render.py assets/source/hero.html --root assets --out assets
```

Repeat for `journey.html`, `roles.html`, `features.html` and `tech.html`. Run from the repository root. The machine must be online (Montserrat is fetched from Google Fonts) and have Edge or Chrome. The renderer warns when content leaves an item box (add `data-bleed` only to shapes meant to bleed), when a file is over 300 KB, and when a requested font did not load; treat each warning as a defect to fix.

## 7. Rules

1. Colours and the logo are never altered; shape and type follow the signature in section 2.
2. Red is never used for body text.
3. Every figure ships as a pair (`name.webp`, `name-dark.webp`), is 2400 px wide, stays under 300 KB, and has alt text in the README that says everything the picture says.
4. A picture states only what the code does. A feature the code does not deliver gets no tile and no mention. Each claim is traced to a source file before it is drawn and re-checked when the code changes.
5. No mock-up of the app's screens, no screenshots.
6. No count that the app does not show; the QR refresh time is a fact of the code and may be stated.
7. Credentials, keys and personal data never appear in an image, a README or this file. Features that handle personal data (identity-card verification for students) appear only as the ticket they unlock, never as a flow.
8. Look at every image in both themes after every render; a script cannot judge whether a picture reads well.

### Claims in the figures and where the code proves them

Paths are under `app/src/main/java/org/com/hcmurs/` unless they start with `app/src/main/res`. Re-check against the code before relying on a row.

| Claim | Proof |
|---|---|
| QR refreshes every 60 seconds | `ui/screens/metro/myticket/TicketQRCodeViewModel.kt` (60 s countdown, refetch on finish) |
| My Ticket has Not used, Used, Expired | `ui/screens/metro/myticket/`, labels in `res/values/strings.xml` |
| Single tickets pay with Stripe; day and student tickets also take PayOS | `ui/screens/stationselection/OrderFareInfo.kt`, `ui/screens/metro/buyticket/OrderInformationScreen.kt` |
| Fare comes from the backend for two stations | `repositories/apis/ticket/FareMatrixApi.kt` |
| Staff land on the scanner, pick route and station, scan for ENTRY or EXIT, each scan goes to the backend | `ui/screens/login/LoginScreen.kt` (staff role), `ui/screens/staffhome/`, `ui/screens/stationselection/StaffStationSelectionScreen.kt`, `ui/screens/scanqr/ScanQRScreen.kt`, `repositories/apis/ticket/TicketApi.kt` |
| Metro news from the backend | `repositories/apis/blog/PublicBlogApi.kt` |
| Push notifications and an in-app list | `FCMService.kt`, `repositories/apis/notification/NotificationApi.kt` |
| Feedback categories Góp ý, Sự cố ứng dụng, Khác | `ui/screens/metro/feedback/CreateFeedback.kt` |
| Metro AI Assistant needs sign-in | `repositories/apis/chat/ChatApi.kt`, `ui/screens/metro/chatbot/` |
| Map of stations and line | `ui/screens/metro/maps/MapsScreen.kt` |
| Partner links Go!Bus, TTGT, Công dân số, TNGo; construction photos | `ui/screens/metro/cooperationlink/`, `ui/screens/metro/constructionimage/` |
| English and Vietnamese; English shows US dollars at a live rate | `app/src/main/res/values`, `values-vi`, `utils/CurrencyManager.kt` |
| Sign in with Google | `oauth/GoogleAuthManager.kt` |
| Layers: screens, ViewModels, repositories, Retrofit and OkHttp to the backend; Hilt modules | `ui/screens/`, `repositories/`, `core/di/` (for example `core/di/NetworkModule.kt`) |
| Every request goes to `base.url` | `core/di/NetworkModule.kt`, `app/build.gradle.kts` (reads `base.url` from `local.properties`) |
| Stack names and versions (Kotlin 2.0.21, Gradle 8.13, AGP 8.9.0, minSdk 26, targetSdk and compileSdk 35, JDK 17 in CI) | `app/build.gradle.kts`, `gradle/libs.versions.toml`, the Gradle wrapper, `.github/workflows/lint-check.yml` |
| Stripe, PayOS checkout in a browser, Firebase Cloud Messaging, osmdroid, CameraX with ML Kit, Auth0 SDK, Coil | dependencies in `app/build.gradle.kts` and `gradle/libs.versions.toml`; usage in `ui/screens/metro/buyticket/`, `FCMService.kt`, `ui/screens/metro/maps/`, `ui/screens/scanqr/` |
| Spotless with ktlint, `spotlessApply` and `spotlessCheck`, license header | root `build.gradle.kts`, `spotless/license-header.kt`, `.github/workflows/lint-check.yml` (runs on pull requests to `develop` and `main`) |
| Branch from `develop`, pull request to `develop` | the branches of the workflow file; a convention, not enforced by code |

Not delivered by the code at the time of writing, so never drawn or mentioned: redeem a code for a ticket, the events screen, the virtual tour, the settings rows, NFC identity check, Facebook sign-in, offline maps, real-time arrivals, a computed route plan.

## 8. Reuse on another platform

A platform's `assets/frontdoor.md` starts from this file: keep sections 2, 3 and 7 (signature, tokens, rules) as they are, copy `shared.css`, `shared.js` and `logo.png`, and rewrite sections 1, 4, 5 and the claims table for that platform's own figures and code. Only the platform's content differs; the colours, the logo, the type and the shapes are shared.

## 9. Checklist before you finish

- Every colour in a page is a token from `shared.css`, or white or black through a role.
- Smallest text is 16px or more, and every text pair is on the allowed list in section 3.
- Both themes rendered and looked at; no warning left from the renderer.
- Every file is under 300 KB and 2400 px wide.
- README alt text matches the picture, every image path exists, every `<details>` block is closed.
- The tables in sections 1 and 4 and the claims table match the sources.
