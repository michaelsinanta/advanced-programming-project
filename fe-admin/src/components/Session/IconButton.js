import { styled } from '@nextui-org/react';

export const IconButton = styled('button', {
    dflex: 'center',
    border: 'none',
    outline: 'none',
    cursor: 'pointer',
    padding: '0',
    margin: '0',
    bg: 'transparent',
    transition: '$default',
    '&:hover': {
        color: '$successLightContrast'
    },
    '&:active': {
        opacity: '0.6'
    }
});
