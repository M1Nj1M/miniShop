# MiniShop (Backend)

MiniShop은 **상품 등록/조회/수정/삭제(Soft Delete)** 및 **주문 생성/조회/취소** 기능을 제공하는 미니 쇼핑몰 프로젝트입니다.

---

## MiniShop 프론트엔드 레포지토리

- 프론트엔드(React/Vite): https://github.com/M1Nj1M/miniShop_front

---

## 백엔드 프로젝트 구조

```text
src/main/java/com/nbcamp/minishop
├─ controller
│  ├─ OrderController
│  └─ ProductController
│
├─ service
│  ├─ OrderService
│  └─ ProductService
│
├─ repository
│  ├─ OrderRepository
│  └─ ProductRepository
│
├─ domain
│  ├─ Order
│  ├─ Product
│  └─ OrderStatus
│
└─ dto
   ├─ order
   │  └─ OrderResponse
   │
   └─ product
      └─ ProductResponse
```
